package com.daita.datn.models.entities;

import com.daita.datn.models.entities.auth.Account;
import jakarta.persistence.*;
import lombok.*;
import com.daita.datn.enums.RecruiterStatus;

@Entity
@Table(name = "recruiters")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Recruiter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recruiter_id")
    private Integer recruiterId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false, unique = true)
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    @Column(length = 100)
    private String position;

    @Column(length = 20)
    private String phone;

    @Column(name = "avatar_url", length = 255)
    private String avatarUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    private RecruiterStatus status = RecruiterStatus.PENDING;
}
