package com.daita.datn.services.implement;

import com.daita.datn.common.constants.Constant;
import com.daita.datn.enums.ErrorCode;
import com.daita.datn.enums.RecruiterStatus;
import com.daita.datn.exceptions.AppException;
import com.daita.datn.models.dto.RecruiterDocumentDTO;
import com.daita.datn.models.entities.Recruiter;
import com.daita.datn.models.entities.RecruiterDocument;
import com.daita.datn.repositories.RecruiterDocumentRepository;
import com.daita.datn.repositories.RecruiterRepository;
import com.daita.datn.services.AccountService;
import com.daita.datn.services.RecruiterDocumentService;
import com.daita.datn.services.S3StorageService;
import com.daita.datn.common.utils.Util;
import com.daita.datn.models.mappers.RecruiterDocumentMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RecruiterDocumentServiceImpl implements RecruiterDocumentService {

    AccountService accountService;
    RecruiterRepository recruiterRepository;
    RecruiterDocumentRepository recruiterDocumentRepository;
    S3StorageService s3StorageService;
    RecruiterDocumentMapper recruiterDocumentMapper;

    @Override
    @Transactional
    public RecruiterDocumentDTO uploadForCurrentRecruiter(MultipartFile file) throws IOException {
        Util.validateFile(
                file,
                Constant.ALLOWED_DOC_CONTENT_TYPES,
                Constant.MAX_UPLOAD_FILE_SIZE_BYTES
        );

        String accountId = accountService.getCurrentAccount().getAccountId();
        Recruiter recruiter = recruiterRepository.findByAccount_AccountId(accountId)
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND, "Recruiter"));

        if (recruiter.getStatus() == RecruiterStatus.REJECTED) {
            throw new AppException(ErrorCode.OPERATION_NOT_ALLOWED, "Recruiter is rejected");
        }

        String key = s3StorageService.uploadAndReturnKey(file, "recruiter-docs/" + recruiter.getRecruiterId());

        RecruiterDocument doc = recruiterDocumentMapper.toEntity(
                recruiter,
                key,
                file.getOriginalFilename(),
                file.getContentType()
        );

        RecruiterDocument saved = recruiterDocumentRepository.save(doc);
        return toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RecruiterDocumentDTO> listForCurrentRecruiter() {
        String accountId = accountService.getCurrentAccount().getAccountId();
        Recruiter recruiter = recruiterRepository.findByAccount_AccountId(accountId)
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND, "Recruiter"));

        return recruiterDocumentRepository.findAllByRecruiter_RecruiterId(recruiter.getRecruiterId())
                .stream()
                .map(this::toDto)
                .toList();
    }

    private RecruiterDocumentDTO toDto(RecruiterDocument doc) {
        String key = doc.getFileKey();
        String downloadUrl = s3StorageService.generatePresignedUrl(key, Duration.ofMinutes(15));

        return recruiterDocumentMapper.toDto(doc, downloadUrl);
    }
}
