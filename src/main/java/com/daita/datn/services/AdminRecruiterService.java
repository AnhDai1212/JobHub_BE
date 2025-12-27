package com.daita.datn.services;

import com.daita.datn.models.dto.RecruiterDTO;
import com.daita.datn.models.dto.RecruiterStatusUpdateRequest;
import com.daita.datn.models.dto.RecruiterDocumentDTO;
import com.daita.datn.models.dto.AccountStatusUpdateRequest;

import java.util.List;

public interface AdminRecruiterService {
    List<RecruiterDTO> getPendingRecruiters();

    RecruiterDTO updateStatus(Integer recruiterId, RecruiterStatusUpdateRequest request);

    List<RecruiterDocumentDTO> getDocuments(Integer recruiterId);

    RecruiterDTO updateAccountStatus(Integer recruiterId, AccountStatusUpdateRequest request);
}
