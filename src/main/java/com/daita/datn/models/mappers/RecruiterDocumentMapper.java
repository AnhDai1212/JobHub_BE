package com.daita.datn.models.mappers;

import com.daita.datn.models.entities.Recruiter;
import com.daita.datn.models.entities.RecruiterDocument;
import com.daita.datn.models.dto.RecruiterDocumentDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RecruiterDocumentMapper {

    @Mapping(target = "recruiter", source = "recruiter")
    @Mapping(target = "fileKey", source = "fileKey")
    @Mapping(target = "fileName", source = "fileName")
    @Mapping(target = "contentType", source = "contentType")
    @Mapping(target = "documentId", ignore = true)
    RecruiterDocument toEntity(Recruiter recruiter, String fileKey, String fileName, String contentType);

    @Mapping(target = "documentId", source = "doc.documentId")
    @Mapping(target = "fileKey", source = "doc.fileKey")
    @Mapping(target = "fileName", source = "doc.fileName")
    @Mapping(target = "contentType", source = "doc.contentType")
    @Mapping(target = "downloadUrl", source = "downloadUrl")
    RecruiterDocumentDTO toDto(RecruiterDocument doc, String downloadUrl);
}
