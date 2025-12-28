package com.daita.datn.models.dto;

import com.daita.datn.enums.JobType;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class JobDTO {
    private Integer jobId;
    private Integer companyId;
    private String companyName;
    private Integer recruiterId;
    private String companyAvatarUrl;
    private String title;
    private String description;
    private String location;
    private String status;
    private Double minSalary;
    private Double maxSalary;
    private JobType jobType;
    private LocalDate deadline;
    private LocalDateTime createdAt;
    private Set<String> categories;
    private Set<String> tags;
    private List<String> requirements;
}
