package com.Unite.UniteMobileApp.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Entity
    @Table(name = "meetings")
    public class Meetings {

        @Id
        @GeneratedValue(strategy = GenerationType.UUID)
        private UUID id;

        @Column(nullable = false, length = 100)
        private String title;

        @Column(name = "start_time", nullable = false)
        private LocalDateTime startTime;

        @Column(name = "end_time", nullable = false)
        private LocalDateTime endTime;

        @Column(nullable = false, length = 20)
        private String status;  // SCHEDULED, IN_PROGRESS, COMPLETED

        @ManyToOne
        @JoinColumn(name = "created_by", nullable = false)
        private Users createdBy;

        @Column(name = "join_code", unique = true)
        private String joinCode;

        @Column(name = "meeting_type", nullable = false)
        private String meetingType; // "VIDEO" or "AUDIO"

        @Transient
        private String joinLink;

        @ManyToOne
        @JoinColumn(name = "creator_id")
        private Users creator;

        @ManyToMany
        @JoinTable(
                name = "meeting_participants",
                joinColumns = @JoinColumn(name = "meeting_id"),
                inverseJoinColumns = @JoinColumn(name = "user_id")
        )
        private Set<Users> participants = new HashSet<>();

        @ManyToOne(optional = true)
        @JoinColumn(name = "team_id", nullable = true)
        private Teams team;

        @Column(length = 1000)
        private String agenda;

        @Column(length = 1000)
        private String notes;

    }