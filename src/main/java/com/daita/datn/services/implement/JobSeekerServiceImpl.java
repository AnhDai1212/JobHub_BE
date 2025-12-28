package com.daita.datn.services.implement;

import com.daita.datn.common.constants.Constant;
import com.daita.datn.enums.ErrorCode;
import com.daita.datn.enums.RoleType;
import com.daita.datn.exceptions.AppException;
import com.daita.datn.models.dto.JobSeekerDTO;
import com.daita.datn.models.dto.JobSeekerCreateRequest;
import com.daita.datn.models.dto.JobSeekerUpdateRequest;
import com.daita.datn.models.dto.SkillDTO;
import com.daita.datn.models.entities.CandidateSkill;
import com.daita.datn.models.entities.JobSeeker;
import com.daita.datn.models.entities.ParsedCv;
import com.daita.datn.models.entities.auth.Account;
import com.daita.datn.models.dto.CvParseResponse;
import com.daita.datn.models.dto.ParsedCvDTO;
import com.daita.datn.models.dto.ParsedCvSaveRequest;
import com.daita.datn.models.dto.BaseSearchDTO;
import com.daita.datn.models.dto.pagination.PageListDTO;
import com.daita.datn.models.mappers.CandidateSkillMapper;
import com.daita.datn.repositories.ParsedCvRepository;
import com.daita.datn.repositories.JobSeekerRepository;
import com.daita.datn.repositories.CandidateSkillRepository;
import com.daita.datn.services.AccountService;
import com.daita.datn.services.JobSeekerService;
import com.daita.datn.services.RoleService;
import com.daita.datn.services.CloudinaryService;
import com.daita.datn.models.mappers.JobSeekerMapper;
import com.daita.datn.models.mappers.ParsedCvMapper;
import com.daita.datn.services.S3StorageService;
import com.daita.datn.common.utils.Util;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.core.io.ByteArrayResource;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JobSeekerServiceImpl implements JobSeekerService {

    private static final Logger logger = LoggerFactory.getLogger(JobSeekerServiceImpl.class);

    AccountService accountService;
    JobSeekerRepository jobSeekerRepository;
    ParsedCvRepository parsedCvRepository;
    CandidateSkillRepository candidateSkillRepository;
    RoleService roleService;
    JobSeekerMapper jobSeekerMapper;
    CandidateSkillMapper candidateSkillMapper;
    S3StorageService s3StorageService;
    CloudinaryService cloudinaryService;
    ParsedCvMapper parsedCvMapper;
    ObjectMapper objectMapper;

    @NonFinal
    @Value("${cv.parser.url}")
    String cvParserUrl;

    @Override
    @Transactional(readOnly = true)
    public JobSeekerDTO getCurrentJobSeeker() {
        Account account = accountService.getCurrentAccount();

        JobSeeker jobSeeker = jobSeekerRepository.findByAccount_AccountId(account.getAccountId())
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND, "JobSeeker"));

        return enrichWithCvUrl(jobSeeker);
    }

    @Override
    @Transactional(readOnly = true)
    public JobSeekerDTO getJobSeekerById(Integer jobSeekerId) {
        JobSeeker jobSeeker = jobSeekerRepository.findById(jobSeekerId)
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND, "JobSeeker"));
        return enrichWithCvUrl(jobSeeker);
    }

    @Override
    @Transactional
    public JobSeekerDTO createProfile(JobSeekerCreateRequest request) {
        Account account = accountService.getCurrentAccount();

        if (jobSeekerRepository.findByAccount_AccountId(account.getAccountId()).isPresent()) {
            throw new AppException(ErrorCode.RESOURCE_ALREADY_EXISTS, "JobSeeker");
        }

        JobSeeker js = jobSeekerMapper.toEntity(request, account);

        JobSeeker saved = jobSeekerRepository.save(js);
        roleService.assignRole(account, RoleType.JOB_SEEKER);

        return enrichWithCvUrl(saved);
    }

    @Override
    @Transactional
    public JobSeekerDTO updateProfile(JobSeekerUpdateRequest request) {
        Account account = accountService.getCurrentAccount();

        JobSeeker jobSeeker = jobSeekerRepository.findByAccount_AccountId(account.getAccountId())
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND, "JobSeeker"));

        jobSeekerMapper.updateFromRequest(request, jobSeeker);
        JobSeeker saved = jobSeekerRepository.save(jobSeeker);

        return enrichWithCvUrl(saved);
    }

    @Override
    @Transactional
    public JobSeekerDTO uploadCv(MultipartFile file) throws IOException {
        Util.validateFile(
                file,
                Constant.ALLOWED_DOC_CONTENT_TYPES,
                Constant.MAX_UPLOAD_FILE_SIZE_BYTES
        );

        Account account = accountService.getCurrentAccount();
        JobSeeker jobSeeker = jobSeekerRepository.findByAccount_AccountId(account.getAccountId())
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND, "JobSeeker"));

        String key = s3StorageService.uploadAndReturnKey(
                file,
                "jobseeker-cv/" + jobSeeker.getJobSeekerId()
        );

        jobSeeker.setCvUrl(key);
        JobSeeker saved = jobSeekerRepository.save(jobSeeker);

        return enrichWithCvUrl(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public CvParseResponse parseCv(MultipartFile file) throws IOException {
        Util.validateFile(
                file,
                Constant.ALLOWED_DOC_CONTENT_TYPES,
                Constant.MAX_UPLOAD_FILE_SIZE_BYTES
        );

        Account account = accountService.getCurrentAccount();
        JobSeeker jobSeeker = jobSeekerRepository.findByAccount_AccountId(account.getAccountId())
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND, "JobSeeker"));

        // Upload file to S3 so FE có thể lưu lại sau khi chỉnh sửa
        String key = s3StorageService.uploadAndReturnKey(
                file,
                "jobseeker-cv/" + jobSeeker.getJobSeekerId()
        );

        // Call parser service
        byte[] bytes = file.getBytes();
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
                    cvParserUrl + "/parse/cv",
                    HttpMethod.POST,
                    requestEntity,
                    Map.class
            ).getBody();
        } catch (Exception e) {
            logger.error("Failed to parse CV via {}: {}", cvParserUrl, e.getMessage(), e);
            throw new AppException(ErrorCode.EXTERNAL_SERVICE_ERROR, e.getMessage());
        }

        if (response == null) {
            throw new AppException(ErrorCode.SERVER_ERROR, "Empty response from parser");
        }

        return CvParseResponse.builder()
                .fileKey(key)
                .rawText(String.valueOf(response.getOrDefault("rawText", "")))
                .parsedData(response.get("entities"))
                .build();
    }

    @Override
    @Transactional
    public ParsedCvDTO saveParsedCv(ParsedCvSaveRequest request) {
        Account account = accountService.getCurrentAccount();
        JobSeeker jobSeeker = jobSeekerRepository.findByAccount_AccountId(account.getAccountId())
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND, "JobSeeker"));

        try {
            String parsedJson = objectMapper.writeValueAsString(request.getParsedData());

            String cvId = UUID.randomUUID().toString();
            ParsedCv entity = parsedCvMapper.toEntity(request, jobSeeker, cvId, parsedJson);

            ParsedCv saved = parsedCvRepository.save(entity);

            Object parsedDataObj = objectMapper.readValue(parsedJson, Object.class);
            return parsedCvMapper.toDto(saved, parsedDataObj);
        } catch (IOException e) {
            throw new AppException(ErrorCode.SERVER_ERROR, "Cannot serialize parsed data");
        }
    }

    @Override
    @Transactional
    public JobSeekerDTO deleteCv() {
        Account account = accountService.getCurrentAccount();
        JobSeeker jobSeeker = jobSeekerRepository.findByAccount_AccountId(account.getAccountId())
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND, "JobSeeker"));

        String key = jobSeeker.getCvUrl();
        if (key == null || key.isBlank()) {
            return enrichWithCvUrl(jobSeeker);
        }

        s3StorageService.deleteObject(key);
        jobSeeker.setCvUrl(null);

        JobSeeker saved = jobSeekerRepository.save(jobSeeker);
        return enrichWithCvUrl(saved);
    }

    @Override
    @Transactional
    public JobSeekerDTO updateAvatar(MultipartFile avatar) throws IOException {
        Util.validateFile(
                avatar,
                Constant.ALLOWED_DOC_CONTENT_TYPES,
                Constant.MAX_UPLOAD_FILE_SIZE_BYTES
        );

        Account account = accountService.getCurrentAccount();
        JobSeeker jobSeeker = jobSeekerRepository.findByAccount_AccountId(account.getAccountId())
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND, "JobSeeker"));

        String url = cloudinaryService.uploadFile(avatar);
        jobSeeker.setAvatarUrl(url);

        JobSeeker saved = jobSeekerRepository.save(jobSeeker);
        return enrichWithCvUrl(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public PageListDTO<JobSeekerDTO> searchJobSeekers(
            BaseSearchDTO<JobSeekerDTO> request
    ) {
        Pageable pageable = Util.toPageable(
                request.getSortedBy(),
                request.getPagination(),
                Constant.JOB_SEEKER_SORT_FIELDS
        );

        String keyword = Util.buildSearchKeyword(request.getSearchedBy());

        Specification<JobSeeker> spec = Util.buildSearchSpec(
                keyword,
                Constant.JOB_SEEKER_SEARCH_FIELDS,
                Constant.JOB_SEEKER_FETCH_RELATIONS,
                null
        );

        Page<JobSeeker> page = jobSeekerRepository.findAll(spec, pageable);
        java.util.List<JobSeekerDTO> rows = page.getContent()
                .stream()
                .map(this::enrichWithCvUrl)
                .toList();

        return new PageListDTO<>(rows, (int) page.getTotalElements());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SkillDTO> listMySkills() {
        JobSeeker jobSeeker = getCurrentJobSeekerEntity();
        List<CandidateSkill> skills = candidateSkillRepository
                .findByJobSeeker_JobSeekerId(jobSeeker.getJobSeekerId());
        return candidateSkillMapper.toDtoList(skills);
    }

    @Override
    @Transactional
    public SkillDTO createSkill(SkillDTO request) {
        JobSeeker jobSeeker = getCurrentJobSeekerEntity();
        CandidateSkill skill = candidateSkillMapper.toEntity(request, jobSeeker);
        CandidateSkill saved = candidateSkillRepository.save(skill);
        return candidateSkillMapper.toDto(saved);
    }

    @Override
    @Transactional
    public SkillDTO updateSkill(Integer skillId, SkillDTO request) {
        JobSeeker jobSeeker = getCurrentJobSeekerEntity();
        CandidateSkill skill = candidateSkillRepository.findById(skillId)
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND, "Skill"));

        if (!Objects.equals(skill.getJobSeeker().getJobSeekerId(), jobSeeker.getJobSeekerId())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        candidateSkillMapper.updateFromDto(request, skill);
        CandidateSkill saved = candidateSkillRepository.save(skill);
        return candidateSkillMapper.toDto(saved);
    }

    @Override
    @Transactional
    public void deleteSkill(Integer skillId) {
        JobSeeker jobSeeker = getCurrentJobSeekerEntity();
        CandidateSkill skill = candidateSkillRepository.findById(skillId)
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND, "Skill"));

        if (!Objects.equals(skill.getJobSeeker().getJobSeekerId(), jobSeeker.getJobSeekerId())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        candidateSkillRepository.delete(skill);
    }

    private JobSeekerDTO enrichWithCvUrl(JobSeeker jobSeeker) {
        JobSeekerDTO dto = jobSeekerMapper.toDto(jobSeeker);
        if (jobSeeker.getCvUrl() != null && !jobSeeker.getCvUrl().isBlank()) {
            dto.setCvUrl(
                    s3StorageService.generatePresignedUrl(
                            jobSeeker.getCvUrl(),
                            Duration.ofMinutes(15)
                    )
            );
        }
        return dto;
    }

    private JobSeeker getCurrentJobSeekerEntity() {
        Account account = accountService.getCurrentAccount();
        return jobSeekerRepository.findByAccount_AccountId(account.getAccountId())
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND, "JobSeeker"));
    }
}
