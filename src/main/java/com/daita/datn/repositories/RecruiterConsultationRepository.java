package com.daita.datn.repositories;

import com.daita.datn.models.entities.RecruiterConsultation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RecruiterConsultationRepository extends JpaRepository<RecruiterConsultation, Long> {
    Optional<RecruiterConsultation> findByRecruiter_RecruiterId(Integer recruiterId);
}
