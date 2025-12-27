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
import com.daita.datn.models.entities.auth.Account;
import com.daita.datn.models.mappers.ApplicationMapper;
import com.daita.datn.repositories.ApplicationRepository;
import com.daita.datn.repositories.JobRepository;
import com.daita.datn.repositories.JobSeekerRepository;
import com.daita.datn.services.AccountService;
import com.daita.datn.services.ApplicationService;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ApplicationServiceImpl implements ApplicationService {

    AccountService accountService;
    JobSeekerRepository jobSeekerRepository;
    JobRepository jobRepository;
    ApplicationRepository applicationRepository;
    ApplicationMapper applicationMapper;

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

        String applicationId = UUID.randomUUID().toString();
        Application application = applicationMapper.toEntity(
                applicationId,
                job,
                jobSeeker,
                LocalDateTime.now(),
                ApplicationStatus.APPLIED.name()
        );

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
}
