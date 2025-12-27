package com.daita.datn.models.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class FavoriteDTO {
    private Integer favoriteId;
    private Integer jobId;
    private Integer jobSeekerId;
    private LocalDateTime createdAt;
}
