package com.daita.datn.controllers;

import com.daita.datn.common.base.ApiResponse;
import com.daita.datn.common.constants.MessageConstant;
import com.daita.datn.models.dto.admin.AdminDashboardChartDTO;
import com.daita.datn.services.AdminDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/dashboard")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminDashboardController {

    private final AdminDashboardService adminDashboardService;

    @GetMapping("/charts")
    public ApiResponse<AdminDashboardChartDTO> getDashboardCharts() {
        AdminDashboardChartDTO dto = adminDashboardService.getDashboardCharts();
        return ApiResponse.<AdminDashboardChartDTO>builder()
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK.getReasonPhrase())
                .message(MessageConstant.DASHBOARD_CHARTS_FETCH_SUCCESS)
                .data(dto)
                .build();
    }
}
