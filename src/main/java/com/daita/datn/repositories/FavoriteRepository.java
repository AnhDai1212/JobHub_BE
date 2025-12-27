package com.daita.datn.repositories;

import com.daita.datn.models.entities.Favorite;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Integer> {
    boolean existsByJobSeeker_JobSeekerIdAndJob_JobId(Integer jobSeekerId, Integer jobId);

    Optional<Favorite> findByJobSeeker_JobSeekerIdAndJob_JobId(Integer jobSeekerId, Integer jobId);

    Page<Favorite> findAllByJobSeeker_JobSeekerId(Integer jobSeekerId, Pageable pageable);
}
