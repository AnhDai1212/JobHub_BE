package com.daita.datn.models.dto;

import com.daita.datn.enums.JobType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobFilterDTO {
    private List<String> locations;
    private List<JobType> jobTypes;
    private Double salaryMin;
    private Double salaryMax;
    private List<Integer> categoryIds;
    private List<Integer> tagIds;
    private List<Integer> companyIds;
}
