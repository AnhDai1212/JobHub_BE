package com.daita.datn.models.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobApplicationsCountRequest {
    private List<Integer> jobIds;
}
