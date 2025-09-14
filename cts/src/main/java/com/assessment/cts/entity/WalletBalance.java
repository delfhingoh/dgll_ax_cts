package com.assessment.cts.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_balance_wallet_currency", columnNames = {"wallet_id", "currency_id"})
    },
    indexes = {
        @Index(name = "idx_balance_wallet_currency", columnList = "wallet_id, currency_id")
    }
)
@Getter
@Setter
public class WalletBalance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_id", nullable = false)
    private Wallet wallet;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currency_id", nullable = false)
    private Currency currency;
    @Column(nullable = false, precision = 28, scale = 8)
    private BigDecimal availableBalance;
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
