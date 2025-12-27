package com.daita.datn.repositories;

import com.daita.datn.models.entities.ParsedCv;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ParsedCvRepository extends JpaRepository<ParsedCv, String> {
    Optional<ParsedCv> findTopByJobSeeker_JobSeekerIdOrderByCreateAtDesc(Integer jobSeekerId);
}
