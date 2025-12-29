import io
import os
import re
from typing import Optional, List

import docx2txt
import fitz  # PyMuPDF
import spacy
import requests
from fastapi import FastAPI, UploadFile, File, Form
from pydantic import BaseModel


RESUME_MODEL_PATH = os.getenv("RESUME_MODEL_PATH", "assets/ResumeModel/output/model-best")
JD_MODEL_PATH = os.getenv("JD_MODEL_PATH", "assets/JdModel/output/model-best")

app = FastAPI(title="CV/JD Parser Service", version="0.1.0")

# Load models once on startup
resume_model = spacy.load(RESUME_MODEL_PATH)
jd_model = spacy.load(JD_MODEL_PATH)

@app.get("/health")
async def health():
    return {"status": "ok"}


class RankWeights(BaseModel):
    skills: float = 0.5
    experience: float = 0.2
    title: float = 0.3


class RankRequest(BaseModel):
    parsedCv: dict
    parsedJd: dict
    weights: RankWeights = RankWeights()


def _extract_text_from_pdf(data: bytes) -> str:
    with fitz.open(stream=data) as doc:
        return "\n".join(page.get_text() for page in doc)


def _extract_text(file: UploadFile) -> str:
    content = file.file.read()
    filename = file.filename or ""
    ext = filename.lower().rsplit(".", 1)[-1] if "." in filename else ""
    if ext == "pdf":
        return _extract_text_from_pdf(content)
    elif ext in ("doc", "docx"):
        # docx2txt expects a path or file-like; BytesIO works
        with io.BytesIO(content) as buf:
            return docx2txt.process(buf)
    else:
        # Fallback: try to decode as text
        try:
            return content.decode("utf-8")
        except Exception:
            return ""


def _parse_with_model(model, text: str) -> dict:
    doc = model(text)
    out = {}
    for ent in doc.ents:
        out.setdefault(ent.label_, []).append(ent.text)
    return out


def _extract_skills_from_text(text: str) -> List[str]:
    lower = text.lower()
    idx = lower.find("skills")
    if idx == -1:
        return []

    section = text[idx:]
    lines = section.splitlines()
    skills = []
    started = False
    for line in lines:
        cleaned = line.strip().strip("-").strip()
        if not cleaned:
            if started:
                continue
            continue

        lowered = cleaned.lower()
        if not started:
            if lowered.startswith("skills"):
                started = True
                if ":" in cleaned:
                    skills.extend(_split_skill_tokens(cleaned.split(":", 1)[1]))
                continue
            if "skills" in lowered:
                started = True
                after = cleaned.split(":", 1)[1] if ":" in cleaned else ""
                skills.extend(_split_skill_tokens(after))
                continue
            continue

        if _is_section_header(lowered):
            break

        skills.extend(_split_skill_tokens(cleaned))

    return _dedupe_skills(skills)


def _is_section_header(line: str) -> bool:
    headers = (
        "responsibilities",
        "requirements",
        "benefits",
        "job description",
        "company introduction",
        "about",
        "what you will do",
        "education",
    )
    return any(line.startswith(h) for h in headers)


def _split_skill_tokens(text: str) -> List[str]:
    if not text:
        return []
    tokens = re.split(r"[,\;\-\u2022]", text)
    out = []
    for token in tokens:
        cleaned = token.strip().strip(".")
        if not cleaned:
            continue
        out.extend(_normalize_skill_phrase(cleaned))
    return out


def _normalize_skill_phrase(text: str) -> List[str]:
    cleaned = text.strip()
    lower = cleaned.lower()
    prefixes = (
        "proficient in",
        "solid understanding of",
        "familiar with",
        "familiarity with",
        "experience with",
        "experience in",
        "knowledge of",
        "skill for",
        "knack for",
        "basic understanding of",
        "proficient understanding of",
        "understanding of",
    )
    for prefix in prefixes:
        if lower.startswith(prefix):
            cleaned = cleaned[len(prefix):].strip()
            lower = cleaned.lower()
            break

    if lower.startswith("such as"):
        cleaned = cleaned[7:].strip()
        lower = cleaned.lower()

    if " such as " in lower:
        cleaned = cleaned.split(" such as ", 1)[1].strip()
        lower = cleaned.lower()

    if "{{" in cleaned or "}}" in cleaned:
        return []

    if _is_noise_skill_phrase(lower):
        return []

    parts = [p.strip() for p in cleaned.split(" and ") if p.strip()]
    return [p for p in parts if _is_skill_like(p)]


def _is_noise_skill_phrase(text: str) -> bool:
    prefixes = (
        "with ",
        "and ",
        "understanding ",
        "creating ",
        "implementing ",
        "support ",
        "its ",
        "weaknesses ",
        "libraries ",
        "or any ",
    )
    return text.startswith(prefixes)


def _is_skill_like(text: str) -> bool:
    tokens = text.split()
    if not tokens or len(tokens) > 4:
        return False

    lower = text.lower()
    if lower.startswith(("with ", "and ")):
        return False

    allowlist = {
        "java",
        "spring",
        "spring boot",
        "mysql",
        "postgresql",
        "mongodb",
        "mvc",
        "jdbc",
        "rest",
        "restful",
        "jvm",
        "git",
        "maven",
        "gradle",
        "ant",
        "spark",
        "play",
        "swing",
        "swt",
        "awt",
        "kafka",
        "docker",
        "kubernetes",
        "aws",
        "gcp",
        "azure",
        "python",
        "node",
        "nodejs",
        "react",
        "angular",
        "vue",
    }

    if lower in allowlist:
        return True

    if any(token.lower() in allowlist for token in tokens):
        return True

    return any(any(ch.isupper() for ch in token) for token in tokens)


def _dedupe_skills(skills: List[str]) -> List[str]:
    seen = set()
    out = []
    for s in skills:
        key = s.strip().lower()
        if not key or key in seen:
            continue
        seen.add(key)
        out.append(s.strip())
    return out


def _normalize_years_list(values: List[str]) -> List[float]:
    years = []
    for p in values:
        parts = str(p).split()
        if not parts:
            continue
        try:
            base_val = float(parts[0])
        except (ValueError, TypeError):
            continue

        text = str(p).lower()
        year = base_val if ("years" in text or "year" in text) else base_val / 12
        if ("months" in text or "month" in text) and len(parts) >= 3:
            try:
                year += float(parts[2]) / 12
            except (ValueError, TypeError):
                pass

        years.append(round(year, 2))
    return years


def _jaccard(a: List[str], b: List[str]) -> float:
    set_a = set(str(x).lower().strip() for x in a if x)
    set_b = set(str(x).lower().strip() for x in b if x)
    if not set_a or not set_b:
        return 0.0
    return len(set_a & set_b) / len(set_a | set_b)


def _get_search_results(search_query: str) -> Optional[str]:
    endpoint = (
        "https://en.wikipedia.org/w/api.php?action=query&list=search&format=json&utf8=1"
        "&redirects=1&srprop=size&origin=*&srsearch="
        + search_query
    )
    try:
        response = requests.get(endpoint, timeout=5)
        response.raise_for_status()
        data = response.json()
        results = data.get("query", {}).get("search", [])
        if results:
            title = results[0].get("title", "")
            if title:
                return _get_summary(title)
    except Exception:
        return None
    return None


def _get_summary(title: str) -> Optional[str]:
    endpoint = (
        "https://en.wikipedia.org/w/api.php?action=query&prop=extracts&format=json"
        "&exsentences=5&explaintext=&origin=*&titles="
        + title
    )
    try:
        response = requests.get(endpoint, timeout=5)
        response.raise_for_status()
        data = response.json()
        results = data.get("query", {}).get("pages", {})
        for result in results.values():
            return result.get("extract", "")
    except Exception:
        return None
    return None


@app.post("/parse/cv")
async def parse_cv(file: UploadFile = File(...), text: Optional[str] = Form(None)):
    raw_text = text or _extract_text(file)
    entities = _parse_with_model(resume_model, raw_text)
    return {"rawText": raw_text, "entities": entities}


@app.post("/parse/jd")
async def parse_jd(file: Optional[UploadFile] = File(None), text: Optional[str] = Form(None)):
    raw_text = text
    if not raw_text and file:
        raw_text = _extract_text(file)
    if not raw_text:
        return {"error": "No text provided"}, 400
    entities = _parse_with_model(jd_model, raw_text)
    fallback_skills = _extract_skills_from_text(raw_text)
    if fallback_skills:
        existing = entities.get("SKILLS") or []
        entities["SKILLS"] = _dedupe_skills(existing + fallback_skills)
    return {"rawText": raw_text, "entities": entities}


@app.post("/rank")
async def rank(req: RankRequest):
    cv = req.parsedCv or {}
    jd = req.parsedJd or {}
    w = req.weights

    resume_worked_as = cv.get("WORKED AS") or cv.get("JOBPOST") or []
    resume_exp_list = cv.get("YEARS OF EXPERIENCE") or cv.get("EXPERIENCE") or []
    resume_experience = _normalize_years_list(resume_exp_list)
    resume_skills = cv.get("SKILLS") or []

    jd_skills = jd.get("SKILLS") or []
    jd_exp_list = jd.get("EXPERIENCE") or []
    jd_experience = _normalize_years_list(jd_exp_list)
    jd_post = jd.get("JOBPOST") or []

    jd_post_lower = [item.lower() for item in jd_post]
    experience_score = 0.0
    title_score = 0.0
    match_index = -1
    result = False
    if resume_worked_as:
        resume_worked_as_lower = [item.lower() for item in resume_worked_as]
        for i, item in enumerate(resume_worked_as_lower):
            if item in jd_post_lower:
                result = True
                match_index = i
                if resume_experience and jd_experience and match_index < len(resume_experience):
                    experience_difference = jd_experience[0] - resume_experience[match_index]
                    if experience_difference <= 0:
                        experience_score = 1.0
                    elif experience_difference <= 1:
                        experience_score = 0.7
                    else:
                        experience_score = 0.0
                break
            else:
                result = False

        title_score = 1.0 if result else 0.0

    expanded_resume_skills = []
    if resume_skills:
        for skill in resume_skills:
            search_query = f"{skill} in technology "
            summary = _get_search_results(search_query)
            if summary:
                expanded_resume_skills.append(summary)

    if jd_skills and expanded_resume_skills:
        count = 0
        for skill in jd_skills:
            skill_lower = str(skill).lower()
            for summary in expanded_resume_skills:
                if skill_lower in summary.lower():
                    count += 1
                    break
        skills_score = 1 - ((len(jd_skills) - count) / len(jd_skills))
    elif jd_skills:
        skills_score = _jaccard(resume_skills, jd_skills)
    else:
        skills_score = 0.0

    skills_score = skills_score * w.skills
    experience_score = experience_score * w.experience
    title_score = title_score * w.title

    total = (skills_score + experience_score + title_score) * 100
    return {
        "score": round(total, 2),
        "skillsScore": round(skills_score, 4),
        "experienceScore": round(experience_score, 4),
        "titleScore": round(title_score, 4),
        "weights": w,
    }
