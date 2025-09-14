package com.assessment.cts.entity;

import com.assessment.cts.enums.Status;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_product_base_quote", columnNames = {"base_currency_id", "quote_currency_id"})
    },
    indexes = {
        @Index(name = "idx_product_symbol", columnList = "symbol")
    }
)
@Getter
@Setter
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "base_currency_id", nullable = false)
    private Currency baseCurrency;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quote_currency_id")
    private Currency quoteCurrency;
    @Column(nullable = false, length = 25)
    private String symbol;  // baseCurrency.code + quoteCurrency?.code
    @Enumerated(EnumType.STRING)
    @Column(nullable=false, length = 25)
    private Status status;
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
