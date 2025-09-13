package com.assessment.cts.entity;

import com.assessment.cts.enums.PriceSource;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
    indexes = {
        @Index(name = "idx_price_product_created", columnList = "product_id, createdAt")
    }
)
public class Price {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(precision = 28, scale = 8)
    private BigDecimal bidPrice;
    @Column(precision = 28, scale = 8)
    private BigDecimal bidQuantity;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private PriceSource bidSource;

    @Column(precision = 28, scale = 8)
    private BigDecimal askPrice;
    @Column(precision = 28, scale = 8)
    private BigDecimal askQuantity;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private PriceSource askSource;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
