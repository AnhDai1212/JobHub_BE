package com.daita.datn.models.entities;

import com.daita.datn.common.base.BaseEntity;
import com.daita.datn.models.entities.auth.Account;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "job_seekers")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobSeeker extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "job_seeker_id")
    private Integer jobSeekerId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false, unique = true)
    private Account account;

    @Column(name = "full_name", length = 100)
    private String fullName;

    @Column(name = "dob")
    private LocalDate dob;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "address", length = 255)
    private String address;

    @Column(name = "cv_url", length = 255)
    private String cvUrl;

    @Column(name = "avatar_url", length = 255)
    private String avatarUrl;

    @Lob
    @Column(name = "bio")
    private String bio;

    @OneToMany(mappedBy = "jobSeeker", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Application> applications;

    @OneToMany(mappedBy = "jobSeeker", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Favorite> favorites;

    @OneToMany(mappedBy = "jobSeeker", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CandidateSkill> skills;

    @OneToMany(mappedBy = "jobSeeker", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<JobRecommendation> jobRecommendations;

    @OneToMany(mappedBy = "jobSeeker", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ParsedCv> parsedCvs;

    public String getFullNameOrUsername() {
        return fullName != null ? fullName : account.getUsername();
    }
}
