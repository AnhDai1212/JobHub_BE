package com.daita.datn.services.implement;

import com.daita.datn.enums.ErrorCode;
import com.daita.datn.enums.RecruiterStatus;
import com.daita.datn.exceptions.AppException;
import com.daita.datn.models.dto.CandidateChatRequest;
import com.daita.datn.models.dto.CandidateChatResponse;
import com.daita.datn.models.entities.Application;
import com.daita.datn.models.entities.CandidateSkill;
import com.daita.datn.models.entities.Job;
import com.daita.datn.models.entities.JobSeeker;
import com.daita.datn.models.entities.ParsedCv;
import com.daita.datn.models.entities.Recruiter;
import com.daita.datn.models.entities.auth.Account;
import com.daita.datn.repositories.ApplicationRepository;
import com.daita.datn.repositories.JobRepository;
import com.daita.datn.repositories.RecruiterRepository;
import com.daita.datn.services.AccountService;
import com.daita.datn.services.CandidateChatService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CandidateChatServiceImpl implements CandidateChatService {

    private static final Logger logger = LoggerFactory.getLogger(CandidateChatServiceImpl.class);

    AccountService accountService;
    RecruiterRepository recruiterRepository;
    JobRepository jobRepository;
    ApplicationRepository applicationRepository;
    ObjectMapper objectMapper;

    @NonFinal
    @Value("${gemini.api-key}")
    String geminiApiKey;

    @NonFinal
    @Value("${gemini.model}")
    String geminiModel;

    @NonFinal
    @Value("${gemini.base-url}")
    String geminiBaseUrl;

    @Override
    @Transactional(readOnly = true)
    public CandidateChatResponse matchCandidate(Integer jobId, CandidateChatRequest request) {
        if (request == null || request.getPrompt() == null || request.getPrompt().isBlank()) {
            throw new AppException(ErrorCode.VALIDATION_ERROR, "Prompt is required");
        }

        Account account = accountService.getCurrentAccount();
        Recruiter recruiter = recruiterRepository
                .findByAccount_AccountId(account.getAccountId())
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND, "Recruiter"));
        if (recruiter.getStatus() != RecruiterStatus.APPROVED) {
            throw new AppException(ErrorCode.OPERATION_NOT_ALLOWED, "Recruiter not approved");
        }

        Job job = jobRepository.findByJobIdAndRecruiter_RecruiterId(jobId, recruiter.getRecruiterId())
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND, "Job"));

        List<Application> applications = applicationRepository.findAllByJob_JobIdWithCandidates(job.getJobId());
        if (applications.isEmpty()) {
            throw new AppException(ErrorCode.RESOURCE_NOT_FOUND, "Application");
        }

        List<Map<String, Object>> candidates = buildCandidates(applications);
            Map<String, Object> llmResponse = requestGemini(request.getPrompt(), candidates);

        String applicationId = String.valueOf(llmResponse.getOrDefault("applicationId", "")).trim();
        if (applicationId.isBlank()) {
            throw new AppException(ErrorCode.SERVER_ERROR, "AI did not return applicationId");
        }

        Application selected = applications.stream()
                .filter(app -> applicationId.equals(app.getApplicationId()))
                .findFirst()
                .orElseThrow(() -> new AppException(ErrorCode.SERVER_ERROR, "AI returned unknown applicationId"));

        String reason = String.valueOf(llmResponse.getOrDefault("reason", "")).trim();

        return CandidateChatResponse.builder()
                .applicationId(selected.getApplicationId())
                .jobSeekerId(selected.getJobSeeker().getJobSeekerId())
                .fullName(selected.getJobSeeker().getFullNameOrUsername())
                .matchingScore(selected.getMatchingScore())
                .reason(reason)
                .build();
    }

    private List<Map<String, Object>> buildCandidates(List<Application> applications) {
        List<Map<String, Object>> candidates = new ArrayList<>();
        for (Application application : applications) {
            JobSeeker jobSeeker = application.getJobSeeker();
            ParsedCv parsedCv = application.getParsedCv();
            Map<String, Object> entry = new HashMap<>();
            entry.put("applicationId", application.getApplicationId());
            entry.put("jobSeekerId", jobSeeker.getJobSeekerId());
            entry.put("fullName", jobSeeker.getFullNameOrUsername());
            entry.put("bio", jobSeeker.getBio());
            entry.put("skills", mapSkillNames(jobSeeker.getSkills()));
            entry.put("matchingScore", application.getMatchingScore());
            entry.put("parsedCv", parseJsonSafe(parsedCv == null ? null : parsedCv.getParsedJson()));
            candidates.add(entry);
        }
        return candidates;
    }

    private List<String> mapSkillNames(Iterable<CandidateSkill> skills) {
        List<String> out = new ArrayList<>();
        if (skills == null) {
            return out;
        }
        for (CandidateSkill skill : skills) {
            if (skill != null && skill.getSkillName() != null && !skill.getSkillName().isBlank()) {
                out.add(skill.getSkillName().trim());
            }
        }
        return out;
    }

    private Object parseJsonSafe(String json) {
        if (json == null || json.isBlank()) {
            return null;
        }
        try {
            return objectMapper.readValue(json, Object.class);
        } catch (Exception e) {
            return json;
        }
    }

    private Map<String, Object> requestGemini(String prompt, List<Map<String, Object>> candidates) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("contents", List.of(Map.of(
                "role", "user",
                "parts", List.of(Map.of(
                        "text", buildUserPrompt(prompt, candidates)
                ))
        )));
        payload.put("generationConfig", Map.of(
                "temperature", 0.2
        ));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(payload, headers);
        RestTemplate restTemplate = new RestTemplate();

        Map<String, Object> response;
        try {
            response = restTemplate.exchange(
                    geminiBaseUrl + "/models/" + geminiModel + ":generateContent?key=" + geminiApiKey,
                    HttpMethod.POST,
                    requestEntity,
                    Map.class
            ).getBody();
        } catch (Exception e) {
            logger.error("Gemini request failed: {}", e.getMessage(), e);
            throw new AppException(ErrorCode.EXTERNAL_SERVICE_ERROR, e.getMessage());
        }

        if (response == null) {
            throw new AppException(ErrorCode.SERVER_ERROR, "Empty response from Gemini");
        }

        Object content = extractContent(response);
        if (content == null) {
            throw new AppException(ErrorCode.SERVER_ERROR, "Gemini response missing content");
        }

        try {
            String json = extractJson(content.toString());
            return objectMapper.readValue(json, Map.class);
        } catch (Exception e) {
            logger.warn("Gemini response is not valid JSON: {}", content);
            throw new AppException(ErrorCode.SERVER_ERROR, "Gemini response is not valid JSON");
        }
    }

    private String buildUserPrompt(String prompt, List<Map<String, Object>> candidates) {
        try {
            return "You are a recruiter assistant. Select the best candidate based on the criteria."
                    + " Return JSON only: {\"applicationId\":\"...\",\"reason\":\"...\"}.\n"
                    + "Criteria: " + prompt + "\nCandidates: " + objectMapper.writeValueAsString(candidates);
        } catch (Exception e) {
            throw new AppException(ErrorCode.SERVER_ERROR, "Cannot build AI prompt");
        }
    }

    private Object extractContent(Map<String, Object> response) {
        Object candidatesObj = response.get("candidates");
        if (!(candidatesObj instanceof List<?> candidates) || candidates.isEmpty()) {
            return null;
        }
        Object first = candidates.get(0);
        if (!(first instanceof Map<?, ?> firstMap)) {
            return null;
        }
        Object contentObj = firstMap.get("content");
        if (!(contentObj instanceof Map<?, ?> content)) {
            return null;
        }
        Object partsObj = content.get("parts");
        if (!(partsObj instanceof List<?> parts) || parts.isEmpty()) {
            return null;
        }
        Object part = parts.get(0);
        if (!(part instanceof Map<?, ?> partMap)) {
            return null;
        }
        Object textObj = partMap.get("text");
        return textObj == null ? null : textObj.toString().trim();
    }

    private String extractJson(String content) {
        String trimmed = content == null ? "" : content.trim();
        int start = trimmed.indexOf('{');
        int end = trimmed.lastIndexOf('}');
        if (start >= 0 && end > start) {
            return trimmed.substring(start, end + 1);
        }
        return trimmed;
    }
}
