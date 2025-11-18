package com.daita.datn.services;

import com.daita.datn.models.dto.CompanyDTO;
import com.daita.datn.models.entities.Company;

public interface CompaniesService {
    CompanyDTO getCompanyById(String companyId);
    CompanyDTO createCompany(Company company);
    Company updateCompany(Company company);
    Void deleteCompany(String companyId);

}
