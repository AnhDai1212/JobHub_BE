package com.daita.datn.repositories;

import com.daita.datn.models.entities.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobRepository extends JpaRepository<Job, Integer>, JpaSpecificationExecutor<Job> {
    List<Job> findAllByRecruiter_RecruiterId(Integer recruiterId);

    Optional<Job> findByJobIdAndRecruiter_RecruiterId(Integer jobId, Integer recruiterId);

    @Query("""
            SELECT j
            FROM Job j
            LEFT JOIN j.applications a
            WHERE (:statuses IS NULL OR j.status IN :statuses)
            GROUP BY j.jobId
            ORDER BY COUNT(a) DESC, j.createAt DESC
            """)
    Page<Job> findMostAppliedJobs(@Param("statuses") List<String> statuses, Pageable pageable);
}
