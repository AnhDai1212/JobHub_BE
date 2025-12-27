package com.daita.datn.models.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class ReviewDTO {
    private Integer reviewId;
    private Integer companyId;
    private String accountId;
    private Integer rating;
    private String reviewText;
    private LocalDateTime reviewDate;
}
