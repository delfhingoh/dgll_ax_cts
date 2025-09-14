package com.assessment.cts.entity;

import com.assessment.cts.enums.ProductType;
import com.assessment.cts.enums.Status;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

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
    @Column(updatable = false)
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        if (this.wuid == null) {
            this.wuid = UUID.randomUUID().toString(); // It can be moved to service layer to generate blockchain address. For now, just using UUID
        }
    }
}
