package com.daita.datn.models.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class NotificationDTO {
    private String notificationId;
    private String accountId;
    private String message;
    private Boolean isRead;
    private LocalDateTime createdAt;
}
