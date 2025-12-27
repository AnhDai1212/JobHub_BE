package com.daita.datn.repositories;

import com.daita.datn.models.entities.Application;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, String> {
    List<Application> findAllByJob_JobId(Integer jobId);

    Page<Application> findAllByJob_JobId(Integer jobId, Pageable pageable);

    Page<Application> findAllByJobSeeker_JobSeekerId(Integer jobSeekerId, Pageable pageable);

    boolean existsByJobSeeker_JobSeekerIdAndJob_JobId(Integer jobSeekerId, Integer jobId);
}
