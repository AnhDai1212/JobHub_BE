package com.daita.datn.repositories;

import com.daita.datn.models.entities.Application;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, String> {
    interface JobApplicationCount {
        Integer getJobId();
        Long getCount();
    }

    List<Application> findAllByJob_JobId(Integer jobId);

    @Query("""
            select distinct a
            from Application a
            join fetch a.jobSeeker js
            left join fetch js.skills
            left join fetch js.account
            left join fetch a.parsedCv
            where a.job.jobId = :jobId
            """)
    List<Application> findAllByJob_JobIdWithCandidates(@Param("jobId") Integer jobId);

    @EntityGraph(attributePaths = {"jobSeeker", "parsedCv"})
    Page<Application> findAllByJob_JobId(Integer jobId, Pageable pageable);

    @Query("""
            select a.job.jobId as jobId, count(a) as count
            from Application a
            where a.job.recruiter.recruiterId = :recruiterId
              and a.job.jobId in :jobIds
            group by a.job.jobId
            """)
    List<JobApplicationCount> countByJobIdsForRecruiter(
            @Param("recruiterId") Integer recruiterId,
            @Param("jobIds") List<Integer> jobIds
    );

    Page<Application> findAllByJobSeeker_JobSeekerId(Integer jobSeekerId, Pageable pageable);

    boolean existsByJobSeeker_JobSeekerIdAndJob_JobId(Integer jobSeekerId, Integer jobId);

    boolean existsByParsedCv_CvIdAndJob_Recruiter_RecruiterId(String cvId, Integer recruiterId);
}
