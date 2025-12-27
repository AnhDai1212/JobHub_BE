package com.daita.datn.services;

import com.daita.datn.models.dto.*;
import com.daita.datn.models.dto.pagination.PageListDTO;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

public interface JobSeekerService {
    JobSeekerDTO getCurrentJobSeeker();

    JobSeekerDTO getJobSeekerById(Integer jobSeekerId);

    JobSeekerDTO createProfile(JobSeekerCreateRequest request);

    JobSeekerDTO updateProfile(JobSeekerUpdateRequest request);

    JobSeekerDTO uploadCv(MultipartFile file) throws IOException;

    CvParseResponse parseCv(MultipartFile file) throws IOException;

    ParsedCvDTO saveParsedCv(ParsedCvSaveRequest request);

    JobSeekerDTO deleteCv();

    JobSeekerDTO updateAvatar(MultipartFile avatar) throws IOException;

    PageListDTO<JobSeekerDTO> searchJobSeekers(BaseSearchDTO<JobSeekerDTO> request);
}
