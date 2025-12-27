package com.daita.datn.services;

import com.daita.datn.models.dto.RecruiterRegisterResponse;
import com.daita.datn.models.dto.UpgradeRecruiterDTO;
import com.daita.datn.models.dto.RecruiterProfileResponse;
import com.daita.datn.models.dto.RecruiterConsultationRequest;
import com.daita.datn.models.dto.RecruiterConsultationResponse;
import com.daita.datn.models.dto.BaseSearchDTO;
import com.daita.datn.models.dto.RecruiterDTO;
import com.daita.datn.models.dto.pagination.PageListDTO;

public interface RecruiterService {
    RecruiterRegisterResponse upgradeToRecruiter(UpgradeRecruiterDTO dto);

    RecruiterProfileResponse getCurrentRecruiter();

    RecruiterConsultationResponse saveConsultation(RecruiterConsultationRequest request);

    RecruiterProfileResponse updateAvatar(org.springframework.web.multipart.MultipartFile avatar) throws java.io.IOException;

    PageListDTO<RecruiterDTO> searchRecruiters(BaseSearchDTO<RecruiterDTO> request);
}
