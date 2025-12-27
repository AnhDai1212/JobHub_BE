package com.daita.datn.repositories;

import com.daita.datn.models.entities.Company;
import com.daita.datn.models.entities.Recruiter;
import com.daita.datn.enums.RecruiterStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface RecruiterRepository extends JpaRepository<Recruiter,Integer>, JpaSpecificationExecutor<Recruiter> {
    boolean existsByAccount_AccountId(String accountId);

    boolean existsByCompany_CompanyId(Integer companyId);

    Optional<Recruiter> findByAccount_AccountId(String accountId);

    List<Recruiter> findAllByStatus(RecruiterStatus status);

    Optional<Recruiter> findByRecruiterId(Integer recruiterId);
}
