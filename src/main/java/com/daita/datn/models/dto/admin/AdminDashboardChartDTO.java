package com.daita.datn.models.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminDashboardChartDTO {
    private List<AdminChartPointDTO> jobPosts;
    private List<AdminChartPointDTO> cvUploads;
}
