package com.daita.datn.services;

import com.daita.datn.models.dto.RecruiterDocumentDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface RecruiterDocumentService {
    RecruiterDocumentDTO uploadForCurrentRecruiter(MultipartFile file) throws IOException;

    List<RecruiterDocumentDTO> listForCurrentRecruiter();
}
