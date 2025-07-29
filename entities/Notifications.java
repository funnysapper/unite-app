package com.Unite.UniteMobileApp.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor

    @Table(name = "notifications")
    public class Notifications {

        @Id
        @GeneratedValue(strategy = GenerationType.UUID)
        private UUID id;

        @Column(name = "user_id", nullable = false)
        private UUID userId;

        @Column(name = "user_name")
        private String userName;

        @Column(nullable = false, length = 30)
        private String type; // MENTION, MEETING, INFO, etc.

        @Column(columnDefinition = "TEXT")
        private String message;


        @Column(name = "is_read", nullable = false)
        private Boolean isRead = false;

        @Column(name = "created_at", nullable = false, updatable = false)
        @CreationTimestamp
        private LocalDateTime createdAt;

    }


