package com.daita.datn.models.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "recruiter_consultations")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecruiterConsultation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "consultation_id")
    private Long consultationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruiter_id", nullable = false)
    private Recruiter recruiter;

    @Column(name = "hiring_position", length = 255, nullable = false)
    private String hiringPosition;

    @Column(name = "industry", length = 255)
    private String industry;

    @Column(name = "budget")
    private Double budget;

    @Column(name = "currency", length = 20)
    private String currency;

    @Column(name = "notes", length = 1000)
    private String notes;
}
