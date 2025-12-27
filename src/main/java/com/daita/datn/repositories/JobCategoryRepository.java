package com.daita.datn.repositories;

import com.daita.datn.models.entities.JobCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobCategoryRepository extends JpaRepository<JobCategory, Integer> {
}
