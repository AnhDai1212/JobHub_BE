package com.daita.datn.repositories;

import com.daita.datn.models.entities.Company;
import com.daita.datn.models.entities.Recruiter;
import com.daita.datn.enums.RecruiterStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface RecruiterRepository extends JpaRepository<Recruiter,Integer>, JpaSpecificationExecutor<Recruiter> {
    boolean existsByAccount_AccountId(String accountId);

    boolean existsByCompany_CompanyId(Integer companyId);

    Optional<Recruiter> findByAccount_AccountId(String accountId);

    @Query("""
            select r
            from Recruiter r
            join fetch r.account
            left join fetch r.company
            where r.status = :status
            """)
    List<Recruiter> findAllByStatus(@Param("status") RecruiterStatus status);

    Optional<Recruiter> findByRecruiterId(Integer recruiterId);
}
