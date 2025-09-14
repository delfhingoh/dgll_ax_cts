package com.assessment.cts.entity;

import com.assessment.cts.enums.Status;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true, updatable = false)
    private String uuid;
    @Column(nullable = false, length = 100)
    private String username;
    @Column(nullable = false, unique = true, length = 100)
    private String email;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 25)
    private Status status;
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
