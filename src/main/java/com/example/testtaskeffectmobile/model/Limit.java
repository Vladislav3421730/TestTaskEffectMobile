package com.example.testtaskeffectmobile.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "limits")
@Data
public class Limit {

    @Id
    private UUID id;

    @Column(name = "daily_limit", nullable = false)
    private BigDecimal dailyLimit;

    @Column(name = "monthly_limit", nullable = false)
    private BigDecimal monthlyLimit;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id", nullable = false)
    private Card card;

}
