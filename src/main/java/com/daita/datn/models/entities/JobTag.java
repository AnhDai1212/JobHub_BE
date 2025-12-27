package com.daita.datn.models.entities;

import jakarta.persistence.*;
import lombok.*;
import java.util.Set;

@Entity
@Table(name = "job_tags")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id")
    private Integer tagId;

    @Column(name = "tag_name", nullable = false, unique = true, length = 50)
    private String tagName;

    @ManyToMany(mappedBy = "tags", fetch = FetchType.LAZY)
    private Set<Job> jobs;
}
