package com.daita.datn.models.entities;

import com.daita.datn.common.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import com.daita.datn.enums.JobType;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "jobs")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Job extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "job_id")
    private Integer jobId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruiter_id")
    private Recruiter recruiter;

    @Column(nullable = false, length = 100)
    private String title;

    @Lob
    private String description;

    @Column(length = 100)
    private String location;

    @Column(length = 20)
    private String status; // OPEN, CLOSED, DRAFT

    @Column(name = "min_salary")
    private Double minSalary;

    @Column(name = "max_salary")
    private Double maxSalary;

    @Enumerated(EnumType.STRING)
    @Column(name = "job_type", length = 50)
    private JobType jobType;

    @Column(name = "deadline")
    private LocalDate deadline;

    // ================== RELATIONSHIPS ==================
    @ManyToMany
    @JoinTable(
            name = "job_category_mapping",
            joinColumns = @JoinColumn(name = "job_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<JobCategory> categories;

    @ManyToMany
    @JoinTable(
            name = "job_tag_mapping",
            joinColumns = @JoinColumn(name = "job_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<JobTag> tags;

    @OneToMany(mappedBy = "job", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Application> applications;

    @OneToMany(mappedBy = "job", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Favorite> favorites;

    @OneToMany(mappedBy = "job", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<JobRecommendation> recommendations;
}
