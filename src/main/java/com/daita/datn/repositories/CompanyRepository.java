package com.daita.datn.repositories;

import com.daita.datn.models.entities.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyRepository
        extends JpaRepository<Company, Integer>,
        JpaSpecificationExecutor<Company> {

    Optional<Company> findByCompanyNameIgnoreCase(String companyName);

    boolean existsByCompanyNameIgnoreCase(String companyName);
}

