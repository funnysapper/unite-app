package com.Unite.UniteMobileApp.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
        @Table(name = "meeting_attendees")
        @NoArgsConstructor
        @AllArgsConstructor
        @Data
        @Builder
        public class MeetingAttendees {

        @Id
        @GeneratedValue(strategy = GenerationType.UUID)
        private UUID id;

        @ManyToOne
        @JoinColumn(name = "meeting_id", nullable = false)
        private Meetings meeting;

        @ManyToOne
        @JoinColumn(name = "user_id", nullable = false)
        private Users user;

        @Column(name = "joined_at", nullable = false)
        private LocalDateTime joinedAt;

        @Column(name = "left_at")
        private LocalDateTime leftAt;

        @Column(length = 20)
        private String role; // HOST, PARTICIPANT

        private String noOfParticipants;

        // Getters and Setters
    }


