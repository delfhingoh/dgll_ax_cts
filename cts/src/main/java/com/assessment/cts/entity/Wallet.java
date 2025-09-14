package com.assessment.cts.entity;

import com.assessment.cts.enums.ProductType;
import com.assessment.cts.enums.Status;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_wallet_user_type", columnNames = {"user_id", "walletType"})
    },
    indexes = {
        @Index(name = "idx_wallet_user", columnList = "user_id")
    }
)
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @Column(nullable = false)
    private String wuid; // wallet address from blockchain
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 25)
    private ProductType walletType;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 25)
    private Status status;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
