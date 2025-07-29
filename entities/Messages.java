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
    @Table(name = "messages")
    public class Messages {

        @Id
        @GeneratedValue(strategy = GenerationType.UUID)
        private UUID id;

        @ManyToOne
        @JoinColumn(name = "sender_id", nullable = false)
        private Users sender;

        @ManyToOne
        @JoinColumn(name = "chat_id", nullable = false)
        private Chat chat;

        @Column(nullable = false, columnDefinition = "TEXT")
        private String content;

        @Column(name = "message_type", nullable = false, length = 20)
        private String messageType; // TEXT, FILE, IMAGE, etc.

       @CreationTimestamp
       @Column(name = "created_at", updatable = false)
        private LocalDateTime createdAt;

        @Column(name = "is_deleted", nullable = false)
        private boolean isDeleted = false;

    }


