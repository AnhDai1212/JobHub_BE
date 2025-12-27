package com.daita.datn.services;

import com.daita.datn.models.dto.BaseSearchDTO;
import com.daita.datn.models.dto.CompanySuggestionDTO;
import com.daita.datn.models.dto.company.CompanyRequestDTO;
import com.daita.datn.models.dto.company.CompanyResponseDTO;
import com.daita.datn.models.dto.company.CompanyUpdateDTO;
import com.daita.datn.models.dto.pagination.PageListDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface CompanyService {

    CompanyResponseDTO createCompany(CompanyRequestDTO requestDTO);

    CompanyResponseDTO getCompanyById(Integer companyId);

    List<CompanyResponseDTO> getAllCompanies();

    CompanyResponseDTO updateCompanyInfo(Integer companyId, CompanyUpdateDTO requestDTO);

    CompanyResponseDTO updateCompanyAvatar(Integer companyId, MultipartFile avatar) throws IOException;

    void deleteCompany(Integer companyId);

    PageListDTO<CompanyResponseDTO> search(BaseSearchDTO<CompanyResponseDTO> request);

    List<CompanySuggestionDTO> suggestCompanies(String keyword);
}
