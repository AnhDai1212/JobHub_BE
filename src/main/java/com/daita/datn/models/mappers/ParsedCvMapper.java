package com.daita.datn.models.mappers;

import com.daita.datn.models.dto.ParsedCvSaveRequest;
import com.daita.datn.models.dto.ParsedCvDTO;
import com.daita.datn.models.entities.JobSeeker;
import com.daita.datn.models.entities.ParsedCv;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ParsedCvMapper {

    @Mapping(target = "cvId", source = "cvId")
    @Mapping(target = "jobSeeker", source = "jobSeeker")
    @Mapping(target = "fileUrl", source = "request.fileKey")
    @Mapping(target = "extractedText", source = "request.rawText")
    @Mapping(target = "parsedJson", source = "parsedJson")
    ParsedCv toEntity(ParsedCvSaveRequest request, JobSeeker jobSeeker, String cvId, String parsedJson);

    @Mapping(target = "cvId", source = "parsedCv.cvId")
    @Mapping(target = "jobSeekerId", source = "parsedCv.jobSeeker.jobSeekerId")
    @Mapping(target = "fileUrl", source = "parsedCv.fileUrl")
    @Mapping(target = "extractedText", source = "parsedCv.extractedText")
    @Mapping(target = "parsedData", source = "parsedData")
    @Mapping(target = "createdAt", source = "parsedCv.createAt")
    ParsedCvDTO toDto(ParsedCv parsedCv, Object parsedData);
}
