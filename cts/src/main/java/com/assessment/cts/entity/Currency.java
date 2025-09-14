package com.assessment.cts.entity;

import com.assessment.cts.enums.CurrencyType;
import com.assessment.cts.enums.Status;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_currency_type_code", columnNames = {"type", "code"})
    },
    indexes = {
        @Index(name = "idx_currency_code", columnList = "code")
    }
)
public class Currency {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 25)
    private CurrencyType type;  // FIAT, CRYPTO
    @Column(nullable = false, length = 25)
    private String code;    // ETH, USDT
    @Column(nullable = false, length = 50)
    private String name;    // Tether
    @Column(nullable = false, length = 10)
    private String symbol;  // $ in unicode
    @Column(nullable = false)
    private int decimals;
    @Enumerated(EnumType.STRING)
    @Column(nullable=false, length = 25)
    private Status status;
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
