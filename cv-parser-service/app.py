import io
import os
from typing import Optional, List

import docx2txt
import fitz  # PyMuPDF
import spacy
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
    skills: float = 0.6
    experience: float = 0.3
    title: float = 0.1


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


def _jaccard(a: List[str], b: List[str]) -> float:
    set_a = set(x.lower().strip() for x in a if x)
    set_b = set(x.lower().strip() for x in b if x)
    if not set_a or not set_b:
        return 0.0
    return len(set_a & set_b) / len(set_a | set_b)


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
    return {"rawText": raw_text, "entities": entities}


@app.post("/rank")
async def rank(req: RankRequest):
    cv = req.parsedCv or {}
    jd = req.parsedJd or {}
    w = req.weights

    cv_skills = cv.get("SKILLS") or []
    jd_skills = jd.get("SKILLS") or []
    skills_score = _jaccard(cv_skills, jd_skills)

    cv_exp = cv.get("EXPERIENCE") or []
    jd_exp = jd.get("EXPERIENCE") or []
    exp_score = 0.0
    if cv_exp and jd_exp:
        try:
            cv_years = _normalize_years(cv_exp[0])
            jd_years = _normalize_years(jd_exp[0])
            diff = jd_years - cv_years
            if diff <= 0:
                exp_score = 1.0
            elif diff <= 1:
                exp_score = 0.7
            else:
                exp_score = 0.0
        except Exception:
            exp_score = 0.0

    cv_post = cv.get("JOBPOST") or cv.get("WORKED AS") or []
    jd_post = jd.get("JOBPOST") or []
    title_score = _jaccard(cv_post, jd_post)

    total = (skills_score * w.skills) + (exp_score * w.experience) + (title_score * w.title)
    return {
        "score": round(total, 4),
        "skillsScore": round(skills_score, 4),
        "experienceScore": round(exp_score, 4),
        "titleScore": round(title_score, 4),
        "weights": w,
    }


def _normalize_years(text: str) -> float:
    # crude parser: expects "X years" or "X months"
    tokens = text.lower().split()
    if not tokens:
        return 0.0
    val = float(tokens[0])
    if "month" in text:
        return round(val / 12.0, 2)
    return val
