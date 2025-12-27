package com.daita.datn.models.entities;

import jakarta.persistence.*;
import lombok.*;
import java.util.Set;

@Entity
@Table(name = "job_categories")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Integer categoryId;

    @Column(name = "category_name", nullable = false, unique = true, length = 100)
    private String categoryName;

    @ManyToMany(mappedBy = "categories", fetch = FetchType.LAZY)
    private Set<Job> jobs;
}
