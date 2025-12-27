package com.daita.datn.models.mappers;

import com.daita.datn.models.dto.ApplicationDTO;
import com.daita.datn.models.dto.ApplicationDetailDTO;
import com.daita.datn.models.dto.ApplicationHistoryDTO;
import com.daita.datn.models.dto.ApplicationStatusUpdateRequest;
import com.daita.datn.models.entities.Application;
import com.daita.datn.models.entities.ApplicationHistory;
import com.daita.datn.models.entities.Job;
import com.daita.datn.models.entities.JobSeeker;
import com.daita.datn.models.entities.ParsedCv;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.time.LocalDateTime;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ApplicationMapper {

    @Mapping(source = "job.jobId", target = "jobId")
    @Mapping(source = "jobSeeker.jobSeekerId", target = "jobSeekerId")
    @Mapping(source = "parsedCv.cvId", target = "parsedCvId")
    ApplicationDTO toDto(Application application);

    @Mapping(source = "job.jobId", target = "jobId")
    @Mapping(source = "job.title", target = "jobTitle")
    @Mapping(source = "jobSeeker.jobSeekerId", target = "jobSeekerId")
    @Mapping(source = "parsedCv.cvId", target = "parsedCvId")
    @Mapping(source = "parsedCv.parsedJson", target = "parsedCvJson")
    ApplicationDetailDTO toDetailDto(Application application);

    ApplicationHistoryDTO toHistoryDto(ApplicationHistory history);

    List<ApplicationDTO> toDtos(Iterable<Application> applications);

    List<ApplicationHistoryDTO> toHistoryDtos(Iterable<ApplicationHistory> histories);

    @Mapping(target = "status", expression = "java(request.getStatus().name())")
    void updateStatus(ApplicationStatusUpdateRequest request, @MappingTarget Application application);

    @Mapping(target = "applicationId", source = "applicationId")
    @Mapping(target = "job", source = "job")
    @Mapping(target = "jobSeeker", source = "jobSeeker")
    @Mapping(target = "parsedCv", source = "parsedCv")
    @Mapping(target = "appliedAt", source = "appliedAt")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "histories", ignore = true)
    Application toEntity(
            String applicationId,
            Job job,
            JobSeeker jobSeeker,
            ParsedCv parsedCv,
            LocalDateTime appliedAt,
            String status
    );

    @Mapping(target = "application", source = "application")
    @Mapping(target = "status", expression = "java(request.getStatus().name())")
    @Mapping(target = "note", source = "request.note")
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    ApplicationHistory toHistoryEntity(Application application, ApplicationStatusUpdateRequest request);
}
