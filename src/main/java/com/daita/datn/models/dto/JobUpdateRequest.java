package com.daita.datn.models.dto;

import com.daita.datn.enums.JobType;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class JobUpdateRequest {

    @Size(max = 100)
    private String title;

    private String description;

    @Size(max = 100)
    private String location;

    private Double minSalary;

    private Double maxSalary;

    private JobType jobType;

    @FutureOrPresent
    private LocalDate deadline;

    @Size(max = 20)
    private String status; // OPEN, CLOSED, DRAFT

    private List<Integer> tagIds;

    private List<Integer> categoryIds;

    private List<String> requirements;
}
