package com.daita.datn.common.base;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@MappedSuperclass
@Data
public abstract class BaseEntity {
    @Id
    @Column(name = "id", updatable = false, columnDefinition = "VARCHAR(36)", nullable = false)
    private String id;

    @CreationTimestamp
    @Column(name = "create_date", nullable = false, updatable = false)
    private LocalDateTime createDate;

    @UpdateTimestamp
    @Column(name = "modified_date", nullable = false)
    private LocalDateTime modifiedDate;
}
