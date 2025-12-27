package com.daita.datn.services.implement;

import com.daita.datn.enums.ErrorCode;
import com.daita.datn.enums.RecruiterStatus;
import com.daita.datn.exceptions.AppException;
import com.daita.datn.models.dto.AccountStatusUpdateRequest;
import com.daita.datn.models.dto.RecruiterDTO;
import com.daita.datn.models.dto.RecruiterStatusUpdateRequest;
import com.daita.datn.models.dto.RecruiterDocumentDTO;
import com.daita.datn.models.entities.Recruiter;
import com.daita.datn.models.entities.RecruiterDocument;
import com.daita.datn.models.mappers.RecruiterMapper;
import com.daita.datn.models.mappers.RecruiterDocumentMapper;
import com.daita.datn.repositories.RecruiterRepository;
import com.daita.datn.repositories.RecruiterDocumentRepository;
import com.daita.datn.services.S3StorageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminRecruiterServiceImpl implements com.daita.datn.services.AdminRecruiterService {

    RecruiterRepository recruiterRepository;
    RecruiterDocumentRepository recruiterDocumentRepository;
    private final S3StorageService s3StorageService;
    RecruiterMapper recruiterMapper;
    RecruiterDocumentMapper recruiterDocumentMapper;

    @Override
    @Transactional(readOnly = true)
    public List<RecruiterDTO> getPendingRecruiters() {
        return recruiterRepository.findAllByStatus(RecruiterStatus.PENDING)
                .stream()
                .map(recruiterMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public RecruiterDTO updateStatus(Integer recruiterId, RecruiterStatusUpdateRequest request) {
        Recruiter recruiter = recruiterRepository.findById(recruiterId)
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND, "Recruiter"));

        RecruiterStatus status = request.getStatus();
        recruiter.setStatus(status);

        if (status == RecruiterStatus.APPROVED && recruiter.getCompany() != null) {
            recruiter.getCompany().setIsApproved(true);
        }

        Recruiter saved = recruiterRepository.save(recruiter);
        return recruiterMapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RecruiterDocumentDTO> getDocuments(Integer recruiterId) {
        Recruiter recruiter = recruiterRepository.findByRecruiterId(recruiterId)
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND, "Recruiter"));

        return recruiterDocumentRepository.findAllByRecruiter_RecruiterId(recruiter.getRecruiterId())
                .stream()
                .map(this::toDtoDocWithUrl)
                .toList();
    }

    @Override
    @Transactional
    public RecruiterDTO updateAccountStatus(Integer recruiterId, AccountStatusUpdateRequest request) {
        Recruiter recruiter = recruiterRepository.findById(recruiterId)
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND, "Recruiter"));

        if (recruiter.getAccount() == null) {
            throw new AppException(ErrorCode.RESOURCE_NOT_FOUND, "Account");
        }

        recruiter.getAccount().setStatus(request.getStatus());

        return recruiterMapper.toDto(recruiterRepository.save(recruiter));
    }

    private RecruiterDocumentDTO toDtoDocWithUrl(RecruiterDocument doc) {
        String key = doc.getFileKey();
        String downloadUrl = s3StorageService.generatePresignedUrl(key, Duration.ofMinutes(15));
        return recruiterDocumentMapper.toDto(doc, downloadUrl);
    }
}
