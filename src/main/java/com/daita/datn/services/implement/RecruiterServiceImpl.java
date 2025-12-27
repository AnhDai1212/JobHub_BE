package com.daita.datn.services.implement;

import com.daita.datn.enums.ErrorCode;
import com.daita.datn.enums.RecruiterStatus;
import com.daita.datn.enums.RoleType;
import com.daita.datn.exceptions.AppException;
import com.daita.datn.models.dto.UpgradeRecruiterDTO;
import com.daita.datn.models.dto.RecruiterProfileResponse;
import com.daita.datn.models.dto.RecruiterConsultationRequest;
import com.daita.datn.models.dto.RecruiterConsultationResponse;
import com.daita.datn.models.dto.RecruiterRegisterResponse;
import com.daita.datn.models.dto.BaseSearchDTO;
import com.daita.datn.models.dto.RecruiterDTO;
import com.daita.datn.models.dto.pagination.PageListDTO;
import com.daita.datn.models.entities.Company;
import com.daita.datn.models.entities.Recruiter;
import com.daita.datn.models.entities.RecruiterConsultation;
import com.daita.datn.models.entities.auth.Account;
import com.daita.datn.models.mappers.RecruiterMapper;
import com.daita.datn.repositories.CompanyRepository;
import com.daita.datn.repositories.RecruiterRepository;
import com.daita.datn.repositories.RecruiterConsultationRepository;
import com.daita.datn.services.AccountService;
import com.daita.datn.services.CloudinaryService;
import com.daita.datn.services.RecruiterService;
import com.daita.datn.services.RoleService;
import com.daita.datn.common.utils.Util;
import com.daita.datn.common.constants.Constant;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RecruiterServiceImpl implements RecruiterService {

    CompanyRepository companyRepository;
    RecruiterRepository recruiterRepository;
    RecruiterConsultationRepository recruiterConsultationRepository;
    RoleService roleService;
    RecruiterMapper recruiterMapper;
    AccountService accountService;
    CloudinaryService cloudinaryService;

    @Override
    @Transactional
    public RecruiterRegisterResponse upgradeToRecruiter(UpgradeRecruiterDTO dto) {

        Account account = accountService.getCurrentAccount();

        if (recruiterRepository.existsByAccount_AccountId(account.getAccountId())) {
            throw new AppException(ErrorCode.RESOURCE_ALREADY_EXISTS, "Recruiter");
        }

        Company company;

        if (dto.getCompanyId() != null) {
            company = companyRepository.findById(dto.getCompanyId())
                    .orElseThrow(() -> new AppException(
                            ErrorCode.RESOURCE_NOT_FOUND, "Company"
                    ));
        } else {
            String companyName = normalize(dto.getCompanyName());

            company = companyRepository
                    .findByCompanyNameIgnoreCase(companyName)
                    .orElseGet(() -> companyRepository.save(
                            recruiterMapper.toCompany(dto, companyName)
                    ));
        }

        Recruiter recruiter = recruiterMapper.toRecruiter(dto, account, company);

        Recruiter saved = recruiterRepository.save(recruiter);

        roleService.assignRole(account, RoleType.RECRUITER);

        return recruiterMapper.toRegisterResponse(saved);
    }

    @Override
    public RecruiterProfileResponse getCurrentRecruiter() {
        return recruiterMapper.toProfileResponse(getCurrentRecruiterEntity());
    }

    @Override
    @Transactional
    public RecruiterConsultationResponse saveConsultation(RecruiterConsultationRequest request) {
        Recruiter recruiter = getCurrentRecruiterEntity();

        RecruiterConsultation consultation = recruiterConsultationRepository
                .findByRecruiter_RecruiterId(recruiter.getRecruiterId())
                .orElseGet(() -> recruiterMapper.toConsultation(request, recruiter));

        recruiterMapper.updateConsultation(request, consultation);

        RecruiterConsultation saved = recruiterConsultationRepository.save(consultation);
        return recruiterMapper.toConsultationResponse(saved);
    }

    @Override
    @Transactional
    public RecruiterProfileResponse updateAvatar(MultipartFile avatar) throws IOException {
        Recruiter recruiter = getCurrentRecruiterEntity();

        Util.validateFile(
                avatar,
                Constant.ALLOWED_DOC_CONTENT_TYPES,
                Constant.MAX_UPLOAD_FILE_SIZE_BYTES
        );

        String url = cloudinaryService.uploadFile(avatar);
        recruiter.setAvatarUrl(url);

        Recruiter saved = recruiterRepository.save(recruiter);
        return recruiterMapper.toProfileResponse(saved);
    }

    @Override
    @Transactional
    public PageListDTO<RecruiterDTO> searchRecruiters(BaseSearchDTO<RecruiterDTO> request) {
        Pageable pageable = Util.toPageable(
                request.getSortedBy(),
                request.getPagination(),
                Constant.RECRUITER_SORT_FIELDS
        );

        String keyword = Util.buildSearchKeyword(request.getSearchedBy());

        Specification<Recruiter> spec = Util.buildSearchSpec(
                keyword,
                Constant.RECRUITER_SEARCH_FIELDS,
                Constant.RECRUITER_FETCH_RELATIONS,
                null
        );

        Page<Recruiter> page = recruiterRepository.findAll(spec, pageable);
        List<RecruiterDTO> rows = page.getContent()
                .stream()
                .map(recruiterMapper::toDto)
                .toList();

        return new PageListDTO<>(rows, (int) page.getTotalElements());
    }

    private String normalize(String name) {
        return name == null ? null : name.trim().replaceAll("\\s+", " ");
    }

    private Recruiter getCurrentRecruiterEntity() {
        Account account = accountService.getCurrentAccount();
        return recruiterRepository
                .findByAccount_AccountId(account.getAccountId())
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND, "Recruiter"));
    }
}

