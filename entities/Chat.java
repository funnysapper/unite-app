package com.Unite.UniteMobileApp.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
    @Table(name = "chats")
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    @Builder
    public class Chat {

        @Id
        @GeneratedValue(strategy = GenerationType.UUID)
        private UUID id;

        private Boolean isGroup = true;

        private LocalDateTime createdAt;

        @ManyToOne
        @JoinColumn(name = "team_id", nullable = true)
        private Teams team;

        @OneToOne
        @JoinColumn(name = "meeting_id", nullable = true)
        private Meetings meeting;

        @ManyToMany
        @JoinTable(
                name = "chat_participants",
                joinColumns = @JoinColumn(name = "chat_id"),
                inverseJoinColumns = @JoinColumn(name = "user_id")
        )
        private List<Users> participants;
    }


