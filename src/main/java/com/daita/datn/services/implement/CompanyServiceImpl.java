package com.daita.datn.services.implement;

import com.daita.datn.common.constants.Constant;
import com.daita.datn.common.constants.MessageConstant;
import com.daita.datn.enums.ErrorCode;
import com.daita.datn.exceptions.AppException;
import com.daita.datn.models.dto.CompanySuggestionDTO;
import com.daita.datn.models.dto.company.CompanyRequestDTO;
import com.daita.datn.models.dto.company.CompanyResponseDTO;
import com.daita.datn.models.dto.company.CompanyUpdateDTO;
import com.daita.datn.models.dto.pagination.PageListDTO;
import com.daita.datn.models.entities.Company;
import com.daita.datn.models.mappers.CompanyMapper;
import com.daita.datn.repositories.CompanyRepository;
import com.daita.datn.services.CloudinaryService;
import com.daita.datn.services.CompanyService;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.daita.datn.common.utils.Util;
import com.daita.datn.models.dto.BaseSearchDTO;
import com.daita.datn.models.dto.pagination.PageListDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CompanyServiceImpl implements CompanyService {

    CompanyRepository companyRepository;
    CompanyMapper companyMapper;
    CloudinaryService cloudinaryService;

    @Override
    public CompanyResponseDTO createCompany(CompanyRequestDTO req) {
        if(companyRepository.existsByCompanyNameIgnoreCase(req.getCompanyName())){
            throw new AppException(ErrorCode.RESOURCE_ALREADY_EXISTS, "Company ");
        }
        Company company = companyMapper.toEntityWithDefaults(req);
        return companyMapper.toDTO(companyRepository.save(company));
    }

    @Override
    public CompanyResponseDTO getCompanyById(Integer companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND, "Company "));
        return companyMapper.toDTO(company);
    }

    @Override
    public List<CompanyResponseDTO> getAllCompanies() {
        return companyRepository.findAll()
                .stream()
                .map(companyMapper::toDTO)
                .toList();
    }

    @Override
    public CompanyResponseDTO updateCompanyInfo(Integer companyId, CompanyUpdateDTO dto) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND, "Company"));

        companyMapper.updateEntity(dto, company);

        return companyMapper.toDTO(companyRepository.save(company));
    }

    @Override
    public CompanyResponseDTO updateCompanyAvatar(Integer companyId, MultipartFile avatar) throws IOException {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND, "Company"));

        String url = cloudinaryService.uploadFile(avatar);
        company.setAvatarUrl(url);

        return companyMapper.toDTO(companyRepository.save(company));
    }

    @Override
    public void deleteCompany(Integer companyId) {
        if (!companyRepository.existsById(companyId))
            throw new AppException(ErrorCode.RESOURCE_NOT_FOUND, "Company");
        companyRepository.deleteById(companyId);
    }

    @Override
    public PageListDTO<CompanyResponseDTO> search(BaseSearchDTO<CompanyResponseDTO> request) {

        Pageable pageable = Util.toPageable(
                request.getSortedBy(),
                request.getPagination(),
                Constant.COMPANY_SORT_FIELDS
        );

        String keyword = Util.buildSearchKeyword(request.getSearchedBy());

        Specification<Company> spec = Util.buildSearchSpec(
                keyword,
                Constant.COMPANY_SEARCH_FIELDS,
                null,
                null
        );

        Page<Company> page = companyRepository.findAll(spec, pageable);

        List<CompanyResponseDTO> rows = page.getContent()
                .stream()
                .map(companyMapper::toDTO)
                .toList();

        return new PageListDTO<>(rows, (int) page.getTotalElements());
    }
    @Override
    public List<CompanySuggestionDTO> suggestCompanies(String keyword) {

        String searchKeyword = Util.buildSearchKeyword(keyword);

        Specification<Company> spec = Util.buildSearchSpec(
                searchKeyword,
                Constant.COMPANY_SEARCH_FIELDS,
                null,     // không fetch
                null      // không isActive
        );

        return companyRepository.findAll(spec)
                .stream()
                .limit(10)
                .map(companyMapper::toSuggestion)
                .toList();
    }


}
