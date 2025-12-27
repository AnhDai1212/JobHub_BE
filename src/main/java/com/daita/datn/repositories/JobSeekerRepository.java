package com.daita.datn.repositories;

import com.daita.datn.models.entities.JobSeeker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JobSeekerRepository extends JpaRepository<JobSeeker, Integer>, JpaSpecificationExecutor<JobSeeker> {
    Optional<JobSeeker> findByAccount_AccountId(String accountId);
}
