package com.daita.datn.models.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "candidate_skills")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CandidateSkill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "skill_id")
    private Integer skillId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_seeker_id", nullable = false)
    private JobSeeker jobSeeker;

    @Column(name = "skill_name", length = 100)
    private String skillName;

    @Column(name = "proficiency_level", length = 100)
    private String proficiencyLevel;
}
