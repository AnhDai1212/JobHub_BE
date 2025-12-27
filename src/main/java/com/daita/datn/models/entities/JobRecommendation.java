package com.daita.datn.models.entities;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "job_recommendations")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobRecommendation {

    @Id
    @Column(name = "recommendation_id", columnDefinition = "VARCHAR(36)")
    private String recommendationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_seeker_id", nullable = false)
    private JobSeeker jobSeeker;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

    @Column(precision = 5, scale = 4, nullable = false)
    private BigDecimal score;
}