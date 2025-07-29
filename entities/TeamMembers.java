package com.Unite.UniteMobileApp.entities;
import jakarta.persistence.*;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "team_members")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamMembers {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 20)
    private String role;

    @Column(name = "joined_at", nullable = false)
    private LocalDateTime joinedAt;

    // Replace full Users entity with specific fields
    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "user_name")
    private String userName;

    // Replace full addedByUser entity with specific fields
    @Column(name = "added_by_user_id", nullable = false)
    private UUID addedByUserId;

    @Column(name = "added_by_user_name")
    private String addedByUserName;

    // Replace full Teams entity with specific fields
    @Column(name = "team_id", nullable = false)
    private UUID teamId;

    @Column(name = "team_name")
    private String teamName;

    public TeamMembers(UUID teamId, String teamName, String userName,
                       String role) {
        this.teamId = teamId;
        this.teamName = teamName;
        this.userName = userName;
        this.role = role;
    }
}

