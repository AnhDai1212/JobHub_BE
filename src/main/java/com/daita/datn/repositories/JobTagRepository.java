package com.daita.datn.repositories;

import com.daita.datn.models.entities.JobTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobTagRepository extends JpaRepository<JobTag, Integer> {
}
