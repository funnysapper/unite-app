package com.Unite.UniteMobileApp.repositories;

import com.Unite.UniteMobileApp.entities.Meetings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.Optional;

public interface MeetingsRepository extends JpaRepository<Meetings, UUID> {
    List<Meetings> findByTeamId(UUID teamId);
    List<Meetings> findByParticipants_Id(UUID userId);
    List<Meetings> findByCreatedBy_Id(UUID userId);
    @Query("SELECT m FROM Meetings m JOIN m.participants p WHERE p.id = :userId AND m.startTime < :end AND m.endTime > :start")
    List<Meetings> findMeetingsForUserInRange(
            @Param("userId") UUID userId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    Optional<Meetings> findByJoinCode(String joinCode);
}



