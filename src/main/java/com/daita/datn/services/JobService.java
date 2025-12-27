package com.daita.datn.services;

import com.daita.datn.models.dto.JobCreateRequest;
import com.daita.datn.models.dto.JobDTO;
import com.daita.datn.models.dto.JobUpdateRequest;
import com.daita.datn.models.dto.JobFilterDTO;
import com.daita.datn.models.dto.ApplicationDTO;
import com.daita.datn.models.dto.JobStatusUpdateRequest;
import com.daita.datn.models.dto.BaseSearchDTO;
import com.daita.datn.models.dto.ApplicationDetailDTO;
import com.daita.datn.models.dto.ApplicationStatusUpdateRequest;
import com.daita.datn.models.dto.pagination.PageListDTO;

public interface JobService {
    JobDTO createJob(JobCreateRequest request);

    JobDTO getJobForCurrentRecruiter(Integer jobId);

    JobDTO getPublicJobById(Integer jobId);

    PageListDTO<JobDTO> getJobsForCurrentRecruiter(BaseSearchDTO<JobFilterDTO> request);

    PageListDTO<JobDTO> searchPublicJobs(BaseSearchDTO<JobFilterDTO> request);

    PageListDTO<JobDTO> getCompanyJobsPublic(Integer companyId, BaseSearchDTO<JobFilterDTO> request);

    PageListDTO<JobDTO> recommendJobs(BaseSearchDTO<Void> request);

    JobDTO updateJob(Integer jobId, JobUpdateRequest request);

    PageListDTO<ApplicationDTO> getApplicationsForJob(Integer jobId, BaseSearchDTO<Void> request);

    void deleteJob(Integer jobId);

    JobDTO updateJobStatus(Integer jobId, JobStatusUpdateRequest request);

    ApplicationDetailDTO getApplicationDetail(String applicationId);

    ApplicationDTO updateApplicationStatus(String applicationId, ApplicationStatusUpdateRequest request);
}
