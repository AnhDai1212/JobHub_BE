package com.daita.datn.models.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "applications")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Application {

    @Id
    @Column(name = "application_id", columnDefinition = "VARCHAR(36)")
    private String applicationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_seeker_id", nullable = false)
    private JobSeeker jobSeeker;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parsed_cv_id")
    private ParsedCv parsedCv;

    @Column(name = "applied_at")
    private LocalDateTime appliedAt;

    @Column(length = 50)
    private String status;

    @Column(name = "matching_score")
    private Double matchingScore;

    @OneToMany(mappedBy = "application", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ApplicationHistory> histories;
}
