package com.daita.datn.services.implement;

import com.daita.datn.common.constants.Constant;
import com.daita.datn.common.utils.Util;
import com.daita.datn.enums.ApplicationStatus;
import com.daita.datn.enums.ErrorCode;
import com.daita.datn.enums.JobStatus;
import com.daita.datn.exceptions.AppException;
import com.daita.datn.models.dto.ApplicationCreateRequest;
import com.daita.datn.models.dto.ApplicationDTO;
import com.daita.datn.models.dto.ApplicationStatusUpdateRequest;
import com.daita.datn.models.dto.BaseSearchDTO;
import com.daita.datn.models.dto.pagination.PageListDTO;
import com.daita.datn.models.entities.Application;
import com.daita.datn.models.entities.ApplicationHistory;
import com.daita.datn.models.entities.Job;
import com.daita.datn.models.entities.JobSeeker;
import com.daita.datn.models.entities.ParsedCv;
import com.daita.datn.models.entities.auth.Account;
import com.daita.datn.models.mappers.ApplicationMapper;
import com.daita.datn.repositories.ApplicationRepository;
import com.daita.datn.repositories.JobRepository;
import com.daita.datn.repositories.JobSeekerRepository;
import com.daita.datn.repositories.ParsedCvRepository;
import com.daita.datn.services.AccountService;
import com.daita.datn.services.ApplicationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ApplicationServiceImpl implements ApplicationService {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationServiceImpl.class);

    AccountService accountService;
    JobSeekerRepository jobSeekerRepository;
    JobRepository jobRepository;
    ApplicationRepository applicationRepository;
    ParsedCvRepository parsedCvRepository;
    ApplicationMapper applicationMapper;
    ObjectMapper objectMapper;

    @NonFinal
    @Value("${cv.parser.url}")
    String cvParserUrl;

    @Override
    @Transactional
    public ApplicationDTO applyJob(ApplicationCreateRequest request) {
        Account account = accountService.getCurrentAccount();

        JobSeeker jobSeeker = jobSeekerRepository.findByAccount_AccountId(account.getAccountId())
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND, "JobSeeker"));

        Job job = jobRepository.findById(request.getJobId())
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND, "Job"));

        if (!JobStatus.OPEN.name().equals(job.getStatus())) {
            throw new AppException(ErrorCode.OPERATION_NOT_ALLOWED, "Job not open");
        }

        if (applicationRepository.existsByJobSeeker_JobSeekerIdAndJob_JobId(
                jobSeeker.getJobSeekerId(), job.getJobId())) {
            throw new AppException(ErrorCode.RESOURCE_ALREADY_EXISTS, "Application");
        }

        ParsedCv parsedCv = resolveParsedCv(request.getParsedCvId(), jobSeeker);

        String applicationId = UUID.randomUUID().toString();
        Application application = applicationMapper.toEntity(
                applicationId,
                job,
                jobSeeker,
                parsedCv,
                LocalDateTime.now(),
                ApplicationStatus.APPLIED.name()
        );
        application.setMatchingScore(buildMatchingScore(job, parsedCv));

        ApplicationStatusUpdateRequest statusRequest = new ApplicationStatusUpdateRequest();
        statusRequest.setStatus(ApplicationStatus.APPLIED);
        statusRequest.setNote("Applied by job seeker");

        ApplicationHistory history = applicationMapper.toHistoryEntity(application, statusRequest);
        application.setHistories(new HashSet<>());
        application.getHistories().add(history);

        Application saved = applicationRepository.save(application);
        return applicationMapper.toDto(saved);
    }

    @Override
    @Transactional
    public ApplicationDTO withdrawApplication(String applicationId) {
        Account account = accountService.getCurrentAccount();

        JobSeeker jobSeeker = jobSeekerRepository.findByAccount_AccountId(account.getAccountId())
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND, "JobSeeker"));

        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND, "Application"));

        if (!application.getJobSeeker().getJobSeekerId().equals(jobSeeker.getJobSeekerId())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        ApplicationStatusUpdateRequest statusRequest = new ApplicationStatusUpdateRequest();
        statusRequest.setStatus(ApplicationStatus.WITHDRAWN);
        statusRequest.setNote("Withdrawn by job seeker");

        applicationMapper.updateStatus(statusRequest, application);
        ApplicationHistory history = applicationMapper.toHistoryEntity(application, statusRequest);
        if (application.getHistories() == null) {
            application.setHistories(new HashSet<>());
        }
        application.getHistories().add(history);

        Application saved = applicationRepository.save(application);
        return applicationMapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public PageListDTO<ApplicationDTO> listMyApplications(BaseSearchDTO<ApplicationDTO> request) {
        Account account = accountService.getCurrentAccount();

        JobSeeker jobSeeker = jobSeekerRepository.findByAccount_AccountId(account.getAccountId())
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND, "JobSeeker"));

        Pageable pageable = Util.toPageable(
                request.getSortedBy(),
                request.getPagination(),
                Constant.APPLICATION_SORT_FIELDS
        );

        Page<Application> page = applicationRepository.findAllByJobSeeker_JobSeekerId(
                jobSeeker.getJobSeekerId(),
                pageable
        );

        java.util.List<ApplicationDTO> rows = page.getContent()
                .stream()
                .map(applicationMapper::toDto)
                .toList();

        return new PageListDTO<>(rows, (int) page.getTotalElements());
    }

    private ParsedCv resolveParsedCv(String parsedCvId, JobSeeker jobSeeker) {
        if (parsedCvId == null || parsedCvId.isBlank()) {
            return null;
        }

        ParsedCv parsedCv = parsedCvRepository.findById(parsedCvId)
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND, "ParsedCv"));

        if (!parsedCv.getJobSeeker().getJobSeekerId().equals(jobSeeker.getJobSeekerId())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        return parsedCv;
    }

    private Double buildMatchingScore(Job job, ParsedCv parsedCv) {
        if (parsedCv == null || parsedCv.getParsedJson() == null || parsedCv.getParsedJson().isBlank()) {
            return null;
        }

        try {
            Map<String, Object> parsedCvMap = objectMapper.readValue(parsedCv.getParsedJson(), Map.class);
            Map<String, Object> parsedJdMap = resolveParsedJd(job);
            if (parsedJdMap == null || parsedJdMap.isEmpty()) {
                return null;
            }

            Map<String, Object> mergedJdMap = mergeJdData(job, parsedCvMap, parsedJdMap);
            Map<String, Object> rankResponse = requestRank(parsedCvMap, mergedJdMap);
            Object score = rankResponse.get("score");
            if (score instanceof Number number) {
                return number.doubleValue();
            }
            if (score != null) {
                return Double.parseDouble(score.toString());
            }
        } catch (Exception e) {
            logger.warn("Failed to calculate matching score for job {}: {}", job.getJobId(), e.getMessage());
        }

        return null;
    }

    private Map<String, Object> mergeJdData(
            Job job,
            Map<String, Object> parsedCv,
            Map<String, Object> parsedJd
    ) {
        Map<String, Object> merged = new HashMap<>();
        if (parsedJd != null) {
            merged.putAll(parsedJd);
        }

        String jdText = buildJdText(job);
        Set<String> skills = new LinkedHashSet<>(toStringSet(parsedJd == null ? null : parsedJd.get("SKILLS")));
        Set<String> jobPosts = new LinkedHashSet<>(toStringSet(parsedJd == null ? null : parsedJd.get("JOBPOST")));
        Set<String> experiences = new LinkedHashSet<>(toStringSet(parsedJd == null ? null : parsedJd.get("EXPERIENCE")));

        if (job != null && job.getTitle() != null && !job.getTitle().isBlank()) {
            jobPosts.add(job.getTitle().trim());
        }
        if (job != null && job.getTags() != null && !job.getTags().isEmpty()) {
            for (var tag : job.getTags()) {
                if (tag != null && tag.getTagName() != null) {
                    String name = tag.getTagName().trim();
                    if (!name.isBlank()) {
                        skills.add(name);
                    }
                }
            }
        }

        Set<String> cvSkills = toStringSet(parsedCv == null ? null : parsedCv.get("SKILLS"));
        if (!jdText.isBlank() && !cvSkills.isEmpty()) {
            String jdLower = jdText.toLowerCase(Locale.ROOT);
            for (String skill : cvSkills) {
                if (containsSkill(jdLower, skill)) {
                    skills.add(skill);
                }
            }
        }

        if (experiences.isEmpty()) {
            experiences.addAll(extractExperienceFromText(jdText));
        }

        if (!skills.isEmpty()) {
            merged.put("SKILLS", skills.stream().toList());
        }
        if (!jobPosts.isEmpty()) {
            merged.put("JOBPOST", jobPosts.stream().toList());
        }
        if (!experiences.isEmpty()) {
            merged.put("EXPERIENCE", experiences.stream().toList());
        }

        return merged;
    }

    private String buildJdText(Job job) {
        if (job == null) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        if (job.getTitle() != null) {
            sb.append(job.getTitle()).append(' ');
        }
        if (job.getDescription() != null) {
            sb.append(job.getDescription()).append(' ');
        }
        if (job.getRequirements() != null && !job.getRequirements().isEmpty()) {
            for (var req : job.getRequirements()) {
                if (req != null && req.getRequirementText() != null) {
                    sb.append(req.getRequirementText()).append(' ');
                }
            }
        }
        return sb.toString().trim();
    }

    private Set<String> toStringSet(Object value) {
        Set<String> out = new LinkedHashSet<>();
        if (value == null) {
            return out;
        }
        if (value instanceof List<?> list) {
            for (Object item : list) {
                if (item != null) {
                    String s = item.toString().trim();
                    if (!s.isBlank()) {
                        out.add(s);
                    }
                }
            }
            return out;
        }
        String s = value.toString().trim();
        if (!s.isBlank()) {
            out.add(s);
        }
        return out;
    }

    private boolean containsSkill(String jdLower, String skill) {
        if (skill == null) {
            return false;
        }
        String s = skill.trim();
        if (s.isBlank()) {
            return false;
        }
        String skillLower = s.toLowerCase(Locale.ROOT);
        if (skillLower.contains(" ")) {
            return jdLower.contains(skillLower);
        }
        Pattern pattern = Pattern.compile("\\b" + Pattern.quote(skillLower) + "\\b");
        return pattern.matcher(jdLower).find();
    }

    private Set<String> extractExperienceFromText(String text) {
        Set<String> out = new LinkedHashSet<>();
        if (text == null || text.isBlank()) {
            return out;
        }
        Pattern pattern = Pattern.compile("(\\d+(?:\\.\\d+)?)\\s*(years?|months?)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            String value = matcher.group(1);
            String unit = matcher.group(2);
            out.add(value + " " + unit);
        }
        return out;
    }

    private Map<String, Object> resolveParsedJd(Job job) throws Exception {
        if (job.getParsedJdJson() != null && !job.getParsedJdJson().isBlank()) {
            return objectMapper.readValue(job.getParsedJdJson(), Map.class);
        }

        if (job.getDescription() == null || job.getDescription().isBlank()) {
            return null;
        }

        Map<String, Object> response = parseJdText(job.getDescription());
        Object entities = response.get("entities");
        if (entities == null) {
            return null;
        }

        String parsedJson = objectMapper.writeValueAsString(entities);
        job.setParsedJdJson(parsedJson);
        jobRepository.save(job);
        return objectMapper.readValue(parsedJson, Map.class);
    }

    private Map<String, Object> parseJdText(String text) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("text", text);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        RestTemplate restTemplate = new RestTemplate();

        Map<String, Object> response;
        try {
            response = restTemplate.exchange(
                    cvParserUrl + "/parse/jd",
                    HttpMethod.POST,
                    requestEntity,
                    Map.class
            ).getBody();
        } catch (Exception e) {
            logger.error("Failed to parse JD via {}: {}", cvParserUrl, e.getMessage(), e);
            throw new AppException(ErrorCode.EXTERNAL_SERVICE_ERROR, e.getMessage());
        }

        if (response == null) {
            throw new AppException(ErrorCode.SERVER_ERROR, "Empty response from parser");
        }

        return response;
    }

    private Map<String, Object> requestRank(Map<String, Object> parsedCv, Map<String, Object> parsedJd) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> weights = new HashMap<>();
        weights.put("skills", 0.5);
        weights.put("experience", 0.2);
        weights.put("title", 0.3);

        Map<String, Object> body = new HashMap<>();
        body.put("parsedCv", parsedCv);
        body.put("parsedJd", parsedJd);
        body.put("weights", weights);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        RestTemplate restTemplate = new RestTemplate();

        Map<String, Object> response;
        try {
            response = restTemplate.exchange(
                    cvParserUrl + "/rank",
                    HttpMethod.POST,
                    requestEntity,
                    Map.class
            ).getBody();
        } catch (Exception e) {
            logger.error("Failed to rank via {}: {}", cvParserUrl, e.getMessage(), e);
            throw new AppException(ErrorCode.EXTERNAL_SERVICE_ERROR, e.getMessage());
        }

        if (response == null) {
            throw new AppException(ErrorCode.SERVER_ERROR, "Empty response from ranker");
        }

        return response;
    }
}
