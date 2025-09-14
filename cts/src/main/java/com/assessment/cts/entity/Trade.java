package com.assessment.cts.entity;

import com.assessment.cts.enums.TradeStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
    indexes = {
            @Index(name = "idx_trade_user", columnList = "user_id")
    }
)
public class Trade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true, updatable = false)
    private String tuid;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "base_currency_id", nullable = false)
    private Currency baseCurrency;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quote_currency_id")
    private Currency quoteCurrency;
    @Column(nullable = false, precision = 28, scale = 8)
    private BigDecimal notionalAmount;
    @Column(nullable = false, precision = 28, scale = 8)
    private BigDecimal rateAmount;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 25)
    private TradeStatus status;
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
