package com.daita.datn.models.dto;

import lombok.*;
import java.time.LocalDateTime;
import java.util.Set;

@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class CompanyDTO {
    private Integer companyId;
    private String companyName;
    private String location;
    private String website;
    private String avatarUrl;
    private Boolean isApproved;
    private LocalDateTime createdAt;
    private Set<JobDTO> jobs;
}
