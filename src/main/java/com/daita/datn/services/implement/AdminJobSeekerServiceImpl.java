package com.daita.datn.services.implement;

import com.daita.datn.enums.ErrorCode;
import com.daita.datn.exceptions.AppException;
import com.daita.datn.models.dto.AccountStatusUpdateRequest;
import com.daita.datn.models.dto.JobSeekerDTO;
import com.daita.datn.models.entities.JobSeeker;
import com.daita.datn.repositories.JobSeekerRepository;
import com.daita.datn.models.mappers.JobSeekerMapper;
import com.daita.datn.services.AdminJobSeekerService;
import com.daita.datn.services.S3StorageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminJobSeekerServiceImpl implements AdminJobSeekerService {

    JobSeekerRepository jobSeekerRepository;
    S3StorageService s3StorageService;
    JobSeekerMapper jobSeekerMapper;

    @Override
    @Transactional
    public JobSeekerDTO updateAccountStatus(Integer jobSeekerId, AccountStatusUpdateRequest request) {
        JobSeeker jobSeeker = jobSeekerRepository.findById(jobSeekerId)
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND, "JobSeeker"));

        if (jobSeeker.getAccount() == null) {
            throw new AppException(ErrorCode.RESOURCE_NOT_FOUND, "Account");
        }

        jobSeeker.getAccount().setStatus(request.getStatus());

        return toDto(jobSeekerRepository.save(jobSeeker));
    }

    private JobSeekerDTO toDto(JobSeeker jobSeeker) {
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
}
