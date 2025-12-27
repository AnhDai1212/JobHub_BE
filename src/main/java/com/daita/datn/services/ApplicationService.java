package com.daita.datn.services;

import com.daita.datn.models.dto.ApplicationCreateRequest;
import com.daita.datn.models.dto.ApplicationDTO;
import com.daita.datn.models.dto.BaseSearchDTO;
import com.daita.datn.models.dto.pagination.PageListDTO;

public interface ApplicationService {
    ApplicationDTO applyJob(ApplicationCreateRequest request);

    ApplicationDTO withdrawApplication(String applicationId);

    PageListDTO<ApplicationDTO> listMyApplications(BaseSearchDTO<ApplicationDTO> request);
}
