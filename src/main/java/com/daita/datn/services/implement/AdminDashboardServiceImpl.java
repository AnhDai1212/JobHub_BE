package com.daita.datn.services.implement;

import com.daita.datn.models.dto.admin.AdminChartPointDTO;
import com.daita.datn.models.dto.admin.AdminDashboardChartDTO;
import com.daita.datn.services.AdminDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AdminDashboardServiceImpl implements AdminDashboardService {

    private static final String JOB_POSTS_SQL =
            "SELECT DATE_FORMAT(create_date, '%Y-%m') AS month, COUNT(*) AS count " +
            "FROM jobs " +
            "GROUP BY DATE_FORMAT(create_date, '%Y-%m') " +
            "ORDER BY month";

    private static final String CV_UPLOADS_SQL =
            "SELECT DATE_FORMAT(create_date, '%Y-%m') AS month, COUNT(*) AS count " +
            "FROM parsed_cvs " +
            "GROUP BY DATE_FORMAT(create_date, '%Y-%m') " +
            "ORDER BY month";

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    @Transactional(readOnly = true)
    public AdminDashboardChartDTO getDashboardCharts() {
        List<AdminChartPointDTO> jobPosts =
                queryChart(JOB_POSTS_SQL);
        List<AdminChartPointDTO> cvUploads =
                queryChart(CV_UPLOADS_SQL);

        return new AdminDashboardChartDTO(jobPosts, cvUploads);
    }

    private List<AdminChartPointDTO> queryChart(String sql) {
        return namedParameterJdbcTemplate.query(
                sql,
                Map.of(),
                (rs, rowNum) -> new AdminChartPointDTO(
                        rs.getString("month"),
                        rs.getLong("count")
                )
        );
    }
}
