package com.daita.datn.models.entities;

import com.daita.datn.models.entities.auth.Account;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ai_logs")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AiLog {

    @Id
    @Column(name = "log_id", columnDefinition = "VARCHAR(36)")
    private String logId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id")
    private Job job;

    @Column(length = 100)
    private String action;

    @Lob
    private String explanation;
}
