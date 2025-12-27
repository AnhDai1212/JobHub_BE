package com.daita.datn.models.dto.company;

import com.daita.datn.models.dto.JobDTO;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class CompanyResponseDTO {
    private Integer companyId;
    private String companyName;
    private String location;
    private String website;
    private String avatarUrl;
    private Boolean isApproved;
    private LocalDateTime createdAt;
}
