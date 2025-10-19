package com.daita.datn.models.entities;

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

    /**
     * Dùng String để lưu JSON (Hibernate sẽ map về TEXT trong MySQL)
     * Nếu dùng MySQL 8 trở lên, bạn có thể dùng annotation @Column(columnDefinition = "JSON")
     */
    @Column(name = "embedding", columnDefinition = "JSON")
    private String embedding;
}
