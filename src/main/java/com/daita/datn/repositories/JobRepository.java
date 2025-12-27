package com.daita.datn.repositories;

import com.daita.datn.models.entities.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobRepository extends JpaRepository<Job, Integer>, JpaSpecificationExecutor<Job> {
    List<Job> findAllByRecruiter_RecruiterId(Integer recruiterId);

    Optional<Job> findByJobIdAndRecruiter_RecruiterId(Integer jobId, Integer recruiterId);
}
