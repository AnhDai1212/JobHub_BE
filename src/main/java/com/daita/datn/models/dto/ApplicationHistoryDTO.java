package com.daita.datn.models.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationHistoryDTO {
    private Integer historyId;
    private String status;
    private String note;
    private LocalDateTime updatedAt;
}
