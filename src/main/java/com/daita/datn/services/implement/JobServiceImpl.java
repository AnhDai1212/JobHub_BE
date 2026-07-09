package com.daita.datn.services.implement;

import com.daita.datn.common.constants.Constant;
import com.daita.datn.common.utils.Util;
import com.daita.datn.enums.ErrorCode;
import com.daita.datn.enums.ApplicationStatus;
import com.daita.datn.enums.JobStatus;
import com.daita.datn.enums.RecruiterStatus;
import com.daita.datn.exceptions.AppException;
import com.daita.datn.models.dto.BaseSearchDTO;
import com.daita.datn.models.dto.JobCreateRequest;
import com.daita.datn.models.dto.JobDTO;
import com.daita.datn.models.dto.JobUpdateRequest;
import com.daita.datn.models.dto.JobFilterDTO;
import com.daita.datn.models.dto.ApplicationDTO;
import com.daita.datn.models.dto.JobStatusUpdateRequest;
import com.daita.datn.models.dto.ApplicationDetailDTO;
import com.daita.datn.models.dto.ApplicationHistoryDTO;
import com.daita.datn.models.dto.ApplicationStatusUpdateRequest;
import com.daita.datn.models.dto.JobApplicationsCountRequest;
import com.daita.datn.models.dto.pagination.PageListDTO;
import com.daita.datn.models.dto.pagination.PaginationDTO;
import com.daita.datn.models.entities.Company;
import com.daita.datn.models.entities.Job;
import com.daita.datn.models.entities.Recruiter;
import com.daita.datn.models.entities.Application;
import com.daita.datn.models.entities.ApplicationHistory;
import com.daita.datn.enums.JobType;
import com.daita.datn.models.entities.JobCategory;
import com.daita.datn.models.entities.JobRequirement;
import com.daita.datn.models.entities.JobSeeker;
import com.daita.datn.models.entities.JobTag;
import com.daita.datn.models.entities.ParsedCv;
import com.daita.datn.models.entities.auth.Account;
import com.daita.datn.repositories.JobRepository;
import com.daita.datn.repositories.JobCategoryRepository;
import com.daita.datn.repositories.JobTagRepository;
import com.daita.datn.repositories.JobSeekerRepository;
import com.daita.datn.repositories.ParsedCvRepository;
import com.daita.datn.repositories.RecruiterRepository;
import com.daita.datn.repositories.ApplicationRepository;
import com.daita.datn.models.mappers.JobMapper;
import com.daita.datn.models.mappers.JobRequirementMapper;
import com.daita.datn.models.mappers.ApplicationMapper;
import com.daita.datn.services.AccountService;
import com.daita.datn.services.EmbeddingService;
import com.daita.datn.services.JobService;
import com.daita.datn.services.S3StorageService;
import com.daita.datn.models.dto.pagination.PaginationDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.HashSet;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JobServiceImpl implements JobService {

    private static final Logger logger = LoggerFactory.getLogger(JobServiceImpl.class);

    RecruiterRepository recruiterRepository;
    JobRepository jobRepository;
    ApplicationRepository applicationRepository;
    JobCategoryRepository jobCategoryRepository;
    JobTagRepository jobTagRepository;
    JobSeekerRepository jobSeekerRepository;
    ParsedCvRepository parsedCvRepository;
    AccountService accountService;
    JobMapper jobMapper;
    JobRequirementMapper jobRequirementMapper;
    ApplicationMapper applicationMapper;
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    ObjectMapper objectMapper;
    S3StorageService s3StorageService;
    EmbeddingService embeddingService;

    @NonFinal
    @Value("${cv.parser.url}")
    String cvParserUrl;

    @Override
    @Transactional
    public JobDTO createJob(JobCreateRequest request) {

        if (request.getMinSalary() != null
                && request.getMaxSalary() != null
                && request.getMinSalary() > request.getMaxSalary()) {
            throw new AppException(ErrorCode.VALIDATION_ERROR, "Min salary cannot exceed max salary");
        }

        Account account = accountService.getCurrentAccount();

        Recruiter recruiter = recruiterRepository
                .findByAccount_AccountId(account.getAccountId())
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND, "Recruiter"));
        assertApproved(recruiter);

        Company company = recruiter.getCompany();
        if (company == null) {
            throw new AppException(ErrorCode.RESOURCE_NOT_FOUND, "Company");
        }

        Job job = jobMapper.toEntity(request, company, recruiter);
        applyTagsAndCategories(job, request.getTagIds(), request.getCategoryIds());
        applyRequirements(job, request.getRequirements());
        applyJobEmbedding(job);

        Job saved = jobRepository.save(job);

        tryParseAndStoreJd(saved);
        return jobMapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public JobDTO getJobForCurrentRecruiter(Integer jobId) {
        Account account = accountService.getCurrentAccount();

        Recruiter recruiter = recruiterRepository
                .findByAccount_AccountId(account.getAccountId())
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND, "Recruiter"));
        assertApproved(recruiter);

        Job job = jobRepository.findByJobIdAndRecruiter_RecruiterId(jobId, recruiter.getRecruiterId())
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND, "Job"));

        return jobMapper.toDto(job);
    }

    @Override
    @Transactional(readOnly = true)
    public JobDTO getPublicJobById(Integer jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND, "Job"));

        return jobMapper.toDto(job);
    }

    @Override
    @Transactional(readOnly = true)
    public PageListDTO<JobDTO> getJobsForCurrentRecruiter(BaseSearchDTO<JobFilterDTO> request) {
        Account account = accountService.getCurrentAccount();

        Recruiter recruiter = recruiterRepository
                .findByAccount_AccountId(account.getAccountId())
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND, "Recruiter"));
        assertApproved(recruiter);

        Pageable pageable = Util.toPageable(
                request.getSortedBy(),
                request.getPagination(),
                Constant.JOB_SORT_FIELDS
        );

        String keyword = Util.buildSearchKeyword(request.getSearchedBy());

        Specification<Job> spec = Util.<Job>buildSearchSpec(
                        keyword,
                        Constant.JOB_SEARCH_FIELDS,
                        Constant.JOB_FETCH_RELATIONS,
                        null
                ).and((root, query, cb) -> {
                    Predicate recruiterPredicate =
                            cb.equal(root.get("recruiter").get("recruiterId"), recruiter.getRecruiterId());
                    Predicate filterPredicate = buildFilterPredicate(root, query, cb, request.getFilter());
                    return cb.and(recruiterPredicate, filterPredicate);
                });

        Page<Job> page = jobRepository.findAll(spec, pageable);

        List<JobDTO> rows = jobMapper.toDtoList(page.getContent());

        return new PageListDTO<>(rows, (int) page.getTotalElements());
    }

    @Override
    @Transactional(readOnly = true)
    public PageListDTO<JobDTO> searchPublicJobs(BaseSearchDTO<JobFilterDTO> request) {
        PaginationDTO pagination = request != null ? request.getPagination() : null;
        if (pagination == null) {
            pagination = new PaginationDTO(0, 20);
        }

        boolean sortByApplications = request != null
                && request.getSortedBy() != null
                && request.getSortedBy().stream()
                .anyMatch(sort -> "applicationsCount".equalsIgnoreCase(sort.getField()));

        if (sortByApplications) {
            List<String> statuses = null;
            JobFilterDTO filter = request.getFilter();
            if (filter != null && filter.getStatuses() != null) {
                List<String> normalized = filter.getStatuses().stream()
                        .filter(Objects::nonNull)
                        .map(String::trim)
                        .filter(value -> !value.isBlank())
                        .map(String::toUpperCase)
                        .toList();
                if (!normalized.isEmpty()) {
                    statuses = normalized;
                }
            }

            Pageable pageable = PageRequest.of(pagination.getPage(), pagination.getPageSize());
            Page<Job> page = jobRepository.findMostAppliedJobs(statuses, pageable);
            List<JobDTO> rows = jobMapper.toDtoList(page.getContent());
            return new PageListDTO<>(rows, (int) page.getTotalElements());
        }

        Pageable pageable = Util.toPageable(
                request.getSortedBy(),
                pagination,
                Constant.JOB_SORT_FIELDS
        );

        String keyword = Util.buildSearchKeyword(request.getSearchedBy());

        Specification<Job> spec = Util.<Job>buildSearchSpec(
                keyword,
                Constant.JOB_SEARCH_FIELDS,
                Constant.JOB_FETCH_RELATIONS,
                null
        ).and((root, query, cb) -> buildFilterPredicate(root, query, cb, request.getFilter()));

        Page<Job> page = jobRepository.findAll(spec, pageable);

        List<JobDTO> rows = jobMapper.toDtoList(page.getContent());

        return new PageListDTO<>(rows, (int) page.getTotalElements());
    }

    @Override
    @Transactional(readOnly = true)
    public PageListDTO<JobDTO> getCompanyJobsPublic(Integer companyId, BaseSearchDTO<JobFilterDTO> request) {
        Pageable pageable = Util.toPageable(
                request.getSortedBy(),
                request.getPagination(),
                Constant.JOB_SORT_FIELDS
        );

        String keyword = Util.buildSearchKeyword(request.getSearchedBy());

        Specification<Job> spec = Util.<Job>buildSearchSpec(
                        keyword,
                        Constant.JOB_SEARCH_FIELDS,
                        Constant.JOB_FETCH_RELATIONS,
                        null
                ).and((root, query, cb) -> {
                    Predicate companyPredicate =
                            cb.equal(root.get("company").get("companyId"), companyId);
                    Predicate filterPredicate = buildFilterPredicate(root, query, cb, request.getFilter());
                    return cb.and(companyPredicate, filterPredicate);
                });

        Page<Job> page = jobRepository.findAll(spec, pageable);

        List<JobDTO> rows = jobMapper.toDtoList(page.getContent());

        return new PageListDTO<>(rows, (int) page.getTotalElements());
    }

    @Override
    @Transactional(readOnly = true)
    public PageListDTO<JobDTO> recommendJobs(BaseSearchDTO<Void> request) {
        PaginationDTO pagination = request != null ? request.getPagination() : null;
        if (pagination == null) {
            pagination = new PaginationDTO(0, 20);
        }

        int page = Math.max(0, pagination.getPage());
        int pageSize = pagination.getPageSize() > 0 ? pagination.getPageSize() : 20;
        int offset = page * pageSize;

        Account account = accountService.getCurrentAccount();
        JobSeeker jobSeeker = jobSeekerRepository.findByAccount_AccountId(account.getAccountId())
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND, "JobSeeker"));

        CvText cvText = buildCvTextWithSource(jobSeeker);

        String sql = "SELECT ranked.job_id, ranked.score "
                + "FROM ( "
                + "  SELECT j.job_id, "
                + "    IFNULL(skill_match.score, 0) AS skill_score, "
                + "    IFNULL(MATCH(j.title, j.description) AGAINST (:cvText IN NATURAL LANGUAGE MODE), 0) AS text_score, "
                + "    IFNULL(pop.score, 0) AS pop_score, "
                + "    ( "
                + "      0.6 * IFNULL(skill_match.score, 0) "
                + "      + 0.3 * IFNULL(MATCH(j.title, j.description) AGAINST (:cvText IN NATURAL LANGUAGE MODE), 0) "
                + "      + 0.1 * IFNULL(pop.score, 0) "
                + "    ) AS score "
                + "  FROM jobs j "
                + "  LEFT JOIN ( "
                + "    SELECT js.job_id, COUNT(DISTINCT js.skill) AS score "
                + "    FROM ( "
                + "      SELECT jtm.job_id, LOWER(jt.tag_name) AS skill "
                + "      FROM job_tag_mapping jtm "
                + "      JOIN job_tags jt ON jt.tag_id = jtm.tag_id "
                + "      UNION ALL "
                + "      SELECT j.job_id, LOWER(jd.skill) AS skill "
                + "      FROM jobs j "
                + "      JOIN JSON_TABLE(COALESCE(j.parsed_jd_json, '{}'), '$.SKILLS[*]' "
                + "        COLUMNS (skill VARCHAR(100) PATH '$') "
                + "      ) jd "
                + "    ) js "
                + "    JOIN ( "
                + "      SELECT LOWER(cs.skill_name) AS skill "
                + "      FROM candidate_skills cs "
                + "      WHERE cs.job_seeker_id = :jobSeekerId "
                + "      UNION ALL "
                + "      SELECT LOWER(cv.skill) AS skill "
                + "      FROM ( "
                + "        SELECT pc.parsed_json "
                + "        FROM parsed_cvs pc "
                + "        WHERE pc.job_seeker_id = :jobSeekerId "
                + "        ORDER BY pc.modified_date DESC, pc.create_date DESC "
                + "        LIMIT 1 "
                + "      ) latest "
                + "      JOIN JSON_TABLE(COALESCE(latest.parsed_json, '{}'), '$.SKILLS[*]' "
                + "        COLUMNS (skill VARCHAR(100) PATH '$') "
                + "      ) cv "
                + "    ) cskills ON cskills.skill = js.skill "
                + "    GROUP BY js.job_id "
                + "  ) skill_match ON skill_match.job_id = j.job_id "
                + "  LEFT JOIN ( "
                + "    SELECT t.job_id, COUNT(*) AS score "
                + "    FROM ( "
                + "      SELECT job_id FROM applications "
                + "      UNION ALL "
                + "      SELECT job_id FROM favorites "
                + "    ) t "
                + "    GROUP BY t.job_id "
                + "  ) pop ON pop.job_id = j.job_id "
                + "  WHERE j.status = 'OPEN' "
                + ") ranked "
                + "WHERE ranked.skill_score > 0 OR ranked.text_score > 0 "
                + "ORDER BY ranked.score DESC "
                + "LIMIT :limit OFFSET :offset";

        Map<String, Object> params = new HashMap<>();
        params.put("jobSeekerId", jobSeeker.getJobSeekerId());
        params.put("cvText", cvText.text());
        params.put("limit", pageSize);
        params.put("offset", offset);

        List<JobScoreRow> rows = namedParameterJdbcTemplate.query(
                sql,
                params,
                (rs, rowNum) -> new JobScoreRow(rs.getInt("job_id"), rs.getDouble("score"))
        );

        if (rows.isEmpty()) {
            int total = (int) jobRepository.count((root, query, cb) ->
                    cb.equal(root.get("status"), JobStatus.OPEN.name()));
            return new PageListDTO<>(List.of(), total);
        }

        List<Integer> jobIds = rows.stream().map(JobScoreRow::jobId).toList();
        Map<Integer, Double> baseScores = new HashMap<>();
        for (JobScoreRow row : rows) {
            baseScores.put(row.jobId(), row.score());
        }

        List<Job> jobs = jobRepository.findAllById(jobIds);
        List<Double> cvEmbedding = resolveCvEmbedding(cvText);
        boolean hasSemantic = cvEmbedding != null && !cvEmbedding.isEmpty();

        List<ScoredJob> scoredJobs = new ArrayList<>();
        for (Job job : jobs) {
            double baseScore = baseScores.getOrDefault(job.getJobId(), 0d);
            double combinedScore = baseScore;
            if (hasSemantic && job.getEmbedding() != null && !job.getEmbedding().isBlank()) {
                List<Double> jobEmbedding = parseEmbedding(job.getEmbedding());
                if (!jobEmbedding.isEmpty()) {
                    double semanticScore = cosineSimilarity(cvEmbedding, jobEmbedding);
                    combinedScore = Constant.RECOMMEND_BASE_WEIGHT * baseScore
                            + Constant.RECOMMEND_SEMANTIC_WEIGHT * semanticScore;
                }
            }
            scoredJobs.add(new ScoredJob(job, combinedScore));
        }

        scoredJobs.sort(Comparator.comparingDouble(ScoredJob::score).reversed());

        List<Job> orderedJobs = scoredJobs.stream().map(ScoredJob::job).toList();
        List<JobDTO> dtos = jobMapper.toDtoList(orderedJobs);
        Map<Integer, Double> scoreMap = new HashMap<>();
        for (ScoredJob scored : scoredJobs) {
            if (scored.job() != null && scored.job().getJobId() != null) {
                scoreMap.put(scored.job().getJobId(), scored.score());
            }
        }
        for (JobDTO dto : dtos) {
            if (dto.getJobId() != null) {
                dto.setRecommendScore(scoreMap.get(dto.getJobId()));
            }
        }
        int total = (int) jobRepository.count((root, query, cb) ->
                cb.equal(root.get("status"), JobStatus.OPEN.name()));
        return new PageListDTO<>(dtos, total);
    }

    @Override
    @Transactional
    public JobDTO updateJob(Integer jobId, JobUpdateRequest request) {
        Account account = accountService.getCurrentAccount();

        Recruiter recruiter = recruiterRepository
                .findByAccount_AccountId(account.getAccountId())
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND, "Recruiter"));
        assertApproved(recruiter);

        Job job = jobRepository.findByJobIdAndRecruiter_RecruiterId(jobId, recruiter.getRecruiterId())
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND, "Job"));

        Double newMin = request.getMinSalary() != null ? request.getMinSalary() : job.getMinSalary();
        Double newMax = request.getMaxSalary() != null ? request.getMaxSalary() : job.getMaxSalary();
        if (newMin != null && newMax != null && newMin > newMax) {
            throw new AppException(ErrorCode.VALIDATION_ERROR, "Min salary cannot exceed max salary");
        }

        jobMapper.updateJobFromRequest(request, job);
        applyTagsAndCategories(job, request.getTagIds(), request.getCategoryIds());
        applyRequirements(job, request.getRequirements());
        if (request.getTitle() != null || request.getDescription() != null) {
            applyJobEmbedding(job);
        }

        Job saved = jobRepository.save(job);

        if (request.getDescription() != null && (saved.getJdFileUrl() == null || saved.getJdFileUrl().isBlank())) {
            tryParseAndStoreJd(saved);
        }
        return jobMapper.toDto(saved);
    }

    @Override
    @Transactional
    public JobDTO uploadJobJd(Integer jobId, MultipartFile file) {
        Util.validateFile(
                file,
                Constant.ALLOWED_DOC_CONTENT_TYPES,
                Constant.MAX_UPLOAD_FILE_SIZE_BYTES
        );

        Account account = accountService.getCurrentAccount();
        Recruiter recruiter = recruiterRepository
                .findByAccount_AccountId(account.getAccountId())
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND, "Recruiter"));
        assertApproved(recruiter);

        Job job = jobRepository.findByJobIdAndRecruiter_RecruiterId(jobId, recruiter.getRecruiterId())
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND, "Job"));

        String key;
        try {
            key = s3StorageService.uploadAndReturnKey(
                    file,
                    "job-jd/" + job.getJobId()
            );
        } catch (java.io.IOException e) {
            throw new AppException(ErrorCode.SERVER_ERROR, "Failed to upload JD file");
        }

        Map<String, Object> response = parseJdFile(file);
        Object entities = response.get("entities");
        if (entities != null) {
            try {
                String parsedJson = objectMapper.writeValueAsString(entities);
                job.setParsedJdJson(parsedJson);
            } catch (Exception e) {
                logger.warn("Failed to serialize JD entities for job {}: {}", job.getJobId(), e.getMessage());
            }
        }

        job.setJdFileUrl(key);
        Job saved = jobRepository.save(job);
        return jobMapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public PageListDTO<ApplicationDTO> getApplicationsForJob(Integer jobId, BaseSearchDTO<Void> request) {
        Account account = accountService.getCurrentAccount();

        Recruiter recruiter = recruiterRepository
                .findByAccount_AccountId(account.getAccountId())
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND, "Recruiter"));
        assertApproved(recruiter);

        jobRepository.findByJobIdAndRecruiter_RecruiterId(jobId, recruiter.getRecruiterId())
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND, "Job"));

        if (request == null) {
            request = new BaseSearchDTO<>();
            request.setPagination(new PaginationDTO(0, 10));
        } else if (request.getPagination() == null) {
            request.setPagination(new PaginationDTO(0, 10));
        }

        Pageable pageable = Util.toPageable(
                request.getSortedBy(),
                request.getPagination(),
                Constant.APPLICATION_SORT_FIELDS
        );

        Page<Application> page =
                applicationRepository.findAllByJob_JobId(jobId, pageable);

        List<ApplicationDTO> rows = page.stream()
                .map(applicationMapper::toDto)
                .toList();

        return new PageListDTO<>(rows, (int) page.getTotalElements());
    }

    @Override
    @Transactional(readOnly = true)
    public ApplicationDetailDTO getApplicationDetail(String applicationId) {
        Account account = accountService.getCurrentAccount();

        Recruiter recruiter = recruiterRepository
                .findByAccount_AccountId(account.getAccountId())
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND, "Recruiter"));
        assertApproved(recruiter);

        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND, "Application"));

        if (!Objects.equals(application.getJob().getRecruiter().getRecruiterId(), recruiter.getRecruiterId())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        ApplicationDetailDTO detail = applicationMapper.toDetailDto(application);
        detail.setHistories(application.getHistories() == null ? List.of() :
                applicationMapper.toHistoryDtos(application.getHistories()));
        return detail;
    }

    @Override
    @Transactional
    public ApplicationDTO updateApplicationStatus(String applicationId, ApplicationStatusUpdateRequest request) {
        Account account = accountService.getCurrentAccount();

        Recruiter recruiter = recruiterRepository
                .findByAccount_AccountId(account.getAccountId())
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND, "Recruiter"));
        assertApproved(recruiter);

        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND, "Application"));

        if (!Objects.equals(application.getJob().getRecruiter().getRecruiterId(), recruiter.getRecruiterId())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        if (request.getStatus() == ApplicationStatus.WITHDRAWN) {
            throw new AppException(ErrorCode.OPERATION_NOT_ALLOWED, "Recruiter cannot withdraw application");
        }

        ApplicationStatus currentStatus = ApplicationStatus.valueOf(application.getStatus());
        validateApplicationStatusTransition(currentStatus, request.getStatus());

        applicationMapper.updateStatus(request, application);

        ApplicationHistory history = applicationMapper.toHistoryEntity(application, request);
        if (application.getHistories() == null) {
            application.setHistories(new HashSet<>());
        }
        application.getHistories().add(history);

        Application saved = applicationRepository.save(application);

        return applicationMapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Map<Integer, Long> countApplicationsForRecruiter(JobApplicationsCountRequest request) {
        if (request == null || request.getJobIds() == null || request.getJobIds().isEmpty()) {
            return Map.of();
        }

        Account account = accountService.getCurrentAccount();
        Recruiter recruiter = recruiterRepository
                .findByAccount_AccountId(account.getAccountId())
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND, "Recruiter"));
        assertApproved(recruiter);

        List<Integer> jobIds = request.getJobIds()
                .stream()
                .filter(Objects::nonNull)
                .distinct()
                .toList();
        if (jobIds.isEmpty()) {
            return Map.of();
        }

        Map<Integer, Long> counts = new HashMap<>();
        for (Integer jobId : jobIds) {
            counts.put(jobId, 0L);
        }

        List<ApplicationRepository.JobApplicationCount> rows =
                applicationRepository.countByJobIdsForRecruiter(recruiter.getRecruiterId(), jobIds);

        for (ApplicationRepository.JobApplicationCount row : rows) {
            if (row.getJobId() != null && row.getCount() != null) {
                counts.put(row.getJobId(), row.getCount());
            }
        }

        return counts;
    }

    private void validateApplicationStatusTransition(ApplicationStatus from, ApplicationStatus to) {
        if (from == to) {
            return;
        }
        switch (from) {
            case APPLIED -> {
                if (to != ApplicationStatus.REVIEWING) {
                    throw new AppException(ErrorCode.OPERATION_NOT_ALLOWED, "Invalid status transition");
                }
            }
            case REVIEWING -> {
                if (to != ApplicationStatus.SHORTLIST && to != ApplicationStatus.REJECTED) {
                    throw new AppException(ErrorCode.OPERATION_NOT_ALLOWED, "Invalid status transition");
                }
            }
            case SHORTLIST -> {
                if (to != ApplicationStatus.HIRED && to != ApplicationStatus.REJECTED) {
                    throw new AppException(ErrorCode.OPERATION_NOT_ALLOWED, "Invalid status transition");
                }
            }
            case REJECTED, HIRED, WITHDRAWN -> throw new AppException(
                    ErrorCode.OPERATION_NOT_ALLOWED,
                    "Cannot transition from terminal status"
            );
        }
    }

    @Override
    @Transactional
    public void deleteJob(Integer jobId) {
        Account account = accountService.getCurrentAccount();

        Recruiter recruiter = recruiterRepository
                .findByAccount_AccountId(account.getAccountId())
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND, "Recruiter"));
        assertApproved(recruiter);

        Job job = jobRepository.findByJobIdAndRecruiter_RecruiterId(jobId, recruiter.getRecruiterId())
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND, "Job"));

        jobRepository.delete(job);
    }

    @Override
    @Transactional
    public JobDTO updateJobStatus(Integer jobId, JobStatusUpdateRequest request) {
        Account account = accountService.getCurrentAccount();

        Recruiter recruiter = recruiterRepository
                .findByAccount_AccountId(account.getAccountId())
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND, "Recruiter"));
        assertApproved(recruiter);

        Job job = jobRepository.findByJobIdAndRecruiter_RecruiterId(jobId, recruiter.getRecruiterId())
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND, "Job"));

        job.setStatus(request.getStatus().name());

        Job saved = jobRepository.save(job);
        return jobMapper.toDto(saved);
    }

    private void assertApproved(Recruiter recruiter) {
        if (recruiter.getStatus() != RecruiterStatus.APPROVED) {
            throw new AppException(ErrorCode.OPERATION_NOT_ALLOWED, "Recruiter not approved");
        }
    }

    private void applyTagsAndCategories(
            Job job,
            List<Integer> tagIds,
            List<Integer> categoryIds
    ) {
        if (tagIds != null) {
            job.setTags(resolveTags(tagIds));
        }

        if (categoryIds != null) {
            job.setCategories(resolveCategories(categoryIds));
        }
    }

    private void applyRequirements(Job job, List<String> requirements) {
        if (requirements == null) {
            return;
        }

        List<String> normalized = requirements.stream()
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(value -> !value.isBlank())
                .toList();

        List<JobRequirement> target = job.getRequirements();
        if (target == null) {
            target = new ArrayList<>();
            job.setRequirements(target);
        }

        target.clear();
        if (normalized.isEmpty()) {
            return;
        }

        for (int i = 0; i < normalized.size(); i++) {
            JobRequirement requirement =
                    jobRequirementMapper.toEntity(normalized.get(i), i + 1, job);
            target.add(requirement);
        }
    }

    private Set<JobTag> resolveTags(List<Integer> tagIds) {
        List<Integer> ids = tagIds.stream()
                .filter(Objects::nonNull)
                .distinct()
                .toList();
        if (ids.isEmpty()) {
            return Collections.emptySet();
        }

        List<JobTag> tags = jobTagRepository.findAllById(ids);
        if (tags.size() != ids.size()) {
            throw new AppException(ErrorCode.RESOURCE_NOT_FOUND, "JobTag");
        }

        return new HashSet<>(tags);
    }

    private Set<JobCategory> resolveCategories(List<Integer> categoryIds) {
        List<Integer> ids = categoryIds.stream()
                .filter(Objects::nonNull)
                .distinct()
                .toList();
        if (ids.isEmpty()) {
            return Collections.emptySet();
        }

        List<JobCategory> categories = jobCategoryRepository.findAllById(ids);
        if (categories.size() != ids.size()) {
            throw new AppException(ErrorCode.RESOURCE_NOT_FOUND, "JobCategory");
        }

        return new HashSet<>(categories);
    }

    private Predicate buildFilterPredicate(
            Root<Job> root,
            CriteriaQuery<?> query,
            CriteriaBuilder cb,
            JobFilterDTO filter
    ) {
        if (filter == null) {
            return cb.conjunction();
        }

        List<Predicate> predicates = new ArrayList<>();

        if (filter.getStatuses() != null && !filter.getStatuses().isEmpty()) {
            List<String> statuses = filter.getStatuses().stream()
                    .filter(Objects::nonNull)
                    .map(String::trim)
                    .filter(value -> !value.isBlank())
                    .map(String::toUpperCase)
                    .toList();
            if (!statuses.isEmpty()) {
                predicates.add(cb.upper(root.get("status")).in(statuses));
            }
        }

        if (filter.getLocations() != null && !filter.getLocations().isEmpty()) {
            List<String> locations = filter.getLocations().stream()
                    .filter(Objects::nonNull)
                    .map(String::trim)
                    .map(String::toLowerCase)
                    .toList();
            predicates.add(cb.lower(root.get("location")).in(locations));
        }

        if (filter.getJobTypes() != null && !filter.getJobTypes().isEmpty()) {
            List<JobType> jobTypes = filter.getJobTypes().stream()
                    .filter(Objects::nonNull)
                    .toList();
            predicates.add(root.get("jobType").in(jobTypes));
        }

        if (filter.getCompanyIds() != null && !filter.getCompanyIds().isEmpty()) {
            predicates.add(root.get("company").get("companyId").in(filter.getCompanyIds()));
        }

        if (filter.getSalaryMin() != null) {
            Predicate maxSalaryPredicate =
                    cb.greaterThanOrEqualTo(root.get("maxSalary"), filter.getSalaryMin());
            predicates.add(cb.or(cb.isNull(root.get("maxSalary")), maxSalaryPredicate));
        }

        if (filter.getSalaryMax() != null) {
            Predicate minSalaryPredicate =
                    cb.lessThanOrEqualTo(root.get("minSalary"), filter.getSalaryMax());
            predicates.add(cb.or(cb.isNull(root.get("minSalary")), minSalaryPredicate));
        }

        boolean hasCategoryFilter = filter.getCategoryIds() != null && !filter.getCategoryIds().isEmpty();
        if (hasCategoryFilter) {
            Join<Job, JobCategory> categoryJoin = root.join("categories", JoinType.LEFT);
            predicates.add(categoryJoin.get("categoryId").in(filter.getCategoryIds()));
            query.distinct(true);
        }

        boolean hasTagFilter = filter.getTagIds() != null && !filter.getTagIds().isEmpty();
        if (hasTagFilter) {
            Join<Job, JobTag> tagJoin = root.join("tags", JoinType.LEFT);
            predicates.add(tagJoin.get("tagId").in(filter.getTagIds()));
            query.distinct(true);
        }

        return predicates.isEmpty() ? cb.conjunction() : cb.and(predicates.toArray(new Predicate[0]));
    }

    private CvText buildCvTextWithSource(JobSeeker jobSeeker) {
        ParsedCv parsedCv = parsedCvRepository
                .findTopByJobSeeker_JobSeekerIdOrderByModifiedDateDescCreateAtDesc(jobSeeker.getJobSeekerId())
                .orElse(null);

        if (parsedCv != null && parsedCv.getExtractedText() != null && !parsedCv.getExtractedText().isBlank()) {
            return new CvText(parsedCv, parsedCv.getExtractedText());
        }

        if (jobSeeker.getSkills() != null && !jobSeeker.getSkills().isEmpty()) {
            String text = jobSeeker.getSkills().stream()
                    .map(skill -> skill.getSkillName() == null ? "" : skill.getSkillName().trim())
                    .filter(s -> !s.isBlank())
                    .distinct()
                    .reduce((a, b) -> a + " " + b)
                    .orElse(" ");
            return new CvText(parsedCv, text);
        }

        return new CvText(parsedCv, " ");
    }

    private List<Double> resolveCvEmbedding(CvText cvText) {
        if (cvText == null) {
            return null;
        }
        ParsedCv parsedCv = cvText.parsedCv();
        if (parsedCv != null && parsedCv.getEmbedding() != null && !parsedCv.getEmbedding().isBlank()) {
            List<Double> parsed = parseEmbedding(parsedCv.getEmbedding());
            if (!parsed.isEmpty()) {
                return parsed;
            }
        }

        List<Double> embedding = embeddingService.embedText(cvText.text());
        if (embedding == null || embedding.isEmpty()) {
            return null;
        }
        return embedding;
    }

    private void applyJobEmbedding(Job job) {
        if (job == null) {
            return;
        }
        String text = buildJobEmbeddingText(job);
        String embeddingJson = toEmbeddingJson(embeddingService.embedText(text));
        if (embeddingJson != null) {
            job.setEmbedding(embeddingJson);
        }
    }

    private String buildJobEmbeddingText(Job job) {
        String title = job.getTitle() == null ? "" : job.getTitle().trim();
        String description = job.getDescription() == null ? "" : job.getDescription().trim();
        String combined = (title + " " + description).trim();
        return combined.isBlank() ? " " : combined;
    }

    private String toEmbeddingJson(List<Double> embedding) {
        if (embedding == null || embedding.isEmpty()) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(embedding);
        } catch (Exception e) {
            logger.warn("Failed to serialize embedding: {}", e.getMessage());
            return null;
        }
    }

    private List<Double> parseEmbedding(String json) {
        if (json == null || json.isBlank()) {
            return List.of();
        }
        try {
            return objectMapper.readValue(json, new TypeReference<List<Double>>() {});
        } catch (Exception e) {
            logger.warn("Failed to parse embedding JSON: {}", e.getMessage());
            return List.of();
        }
    }

    private double cosineSimilarity(List<Double> a, List<Double> b) {
        if (a == null || b == null || a.isEmpty() || b.isEmpty() || a.size() != b.size()) {
            return 0d;
        }
        double dot = 0d;
        double normA = 0d;
        double normB = 0d;
        for (int i = 0; i < a.size(); i++) {
            double x = a.get(i);
            double y = b.get(i);
            dot += x * y;
            normA += x * x;
            normB += y * y;
        }
        if (normA == 0d || normB == 0d) {
            return 0d;
        }
        return dot / (Math.sqrt(normA) * Math.sqrt(normB));
    }

    private void tryParseAndStoreJd(Job job) {
        String description = job.getDescription();
        if (description == null || description.isBlank()) {
            return;
        }

        try {
            Map<String, Object> response = parseJdText(description);
            Object entities = response.get("entities");
            if (entities == null) {
                return;
            }

            String parsedJson = objectMapper.writeValueAsString(entities);
            job.setParsedJdJson(parsedJson);
            jobRepository.save(job);
        } catch (Exception e) {
            logger.warn("Failed to parse JD for job {}: {}", job.getJobId(), e.getMessage());
        }
    }

    private Map<String, Object> parseJdText(String text) {
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("text", text);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

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

    private Map<String, Object> parseJdFile(MultipartFile file) {
        byte[] bytes;
        try {
            bytes = file.getBytes();
        } catch (java.io.IOException e) {
            throw new AppException(ErrorCode.SERVER_ERROR, "Failed to read JD file");
        }

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new ByteArrayResource(bytes) {
            @Override
            public String getFilename() {
                return file.getOriginalFilename();
            }
        });

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

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

    private record JobScoreRow(Integer jobId, Double score) {}

    private record ScoredJob(Job job, Double score) {}

    private record CvText(ParsedCv parsedCv, String text) {}
}
