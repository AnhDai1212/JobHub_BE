package com.daita.datn.models.entities;

import com.daita.datn.common.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "parsed_cvs")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParsedCv extends BaseEntity {

    @Id
    @Column(name = "cv_id", columnDefinition = "VARCHAR(36)")
    private String cvId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_seeker_id", nullable = false)
    private JobSeeker jobSeeker;

    @Column(name = "file_url", length = 255)
    private String fileUrl;

    @Lob
    @Column(name = "extracted_text")
    private String extractedText;

    @Column(name = "embedding", columnDefinition = "JSON")
    private String embedding;

    @Column(name = "parsed_json", columnDefinition = "JSON")
    private String parsedJson;
}
