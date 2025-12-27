package com.daita.datn.models.entities;

import com.daita.datn.models.entities.auth.Account;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "notifications")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

    @Id
    @Column(name = "notification_id", columnDefinition = "VARCHAR(36)")
    private String notificationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Lob
    @Column(name = "message")
    private String message;

    @Column(name = "is_read")
    private Boolean isRead = false;
}
