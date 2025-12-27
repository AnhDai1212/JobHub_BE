package com.daita.datn.models.entities;

import com.daita.datn.common.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import java.util.Set;

@Entity
@Table(name = "companies")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Company extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "company_id")
    private Integer companyId;

    @Column(name = "company_name", nullable = false, length = 100)
    private String companyName;

    @Column(length = 100)
    private String location;

    @Column(length = 255)
    private String website;

    @Column(name = "avatar_url", length = 255)
    private String avatarUrl;

    @Column(name = "is_approved")
    private Boolean isApproved = false;

    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Job> jobs;

    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Recruiter> recruiters;

    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Review> reviews;
}
