package com.daita.datn.services;

import com.daita.datn.models.dto.AccountStatusUpdateRequest;
import com.daita.datn.models.dto.JobSeekerDTO;

public interface AdminJobSeekerService {
    JobSeekerDTO updateAccountStatus(Integer jobSeekerId, AccountStatusUpdateRequest request);
}
