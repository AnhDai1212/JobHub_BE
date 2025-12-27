package com.daita.datn.repositories;

import com.daita.datn.models.entities.RecruiterDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecruiterDocumentRepository extends JpaRepository<RecruiterDocument, Long> {
    List<RecruiterDocument> findAllByRecruiter_RecruiterId(Integer recruiterId);
}
