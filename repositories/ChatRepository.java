package com.Unite.UniteMobileApp.repositories;

import com.Unite.UniteMobileApp.entities.Chat;
import com.Unite.UniteMobileApp.entities.Teams;
import com.Unite.UniteMobileApp.entities.Meetings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChatRepository extends JpaRepository<Chat, UUID> {

        // Get all chats related to a team
        List<Chat> findByTeam(Teams team);

        // Get all chats related to a meeting
        List<Chat> findByMeeting(Meetings meeting);

        // Optional: Get all group chats
        List<Chat> findByIsGroupTrue();



            @Query("""
        SELECT c FROM Chat c
        JOIN c.participants p1
        JOIN c.participants p2
        WHERE p1.id = :userId1 AND p2.id = :userId2
        AND SIZE(c.participants) = 2
        AND c.team IS NULL
        AND c.meeting IS NULL
    """)
            Optional<Chat> findDirectChatBetweenUsers(
                    @Param("userId1") UUID userId1,
                    @Param("userId2") UUID userId2
            );

    }


