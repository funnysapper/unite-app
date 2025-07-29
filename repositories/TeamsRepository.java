package com.Unite.UniteMobileApp.repositories;

import com.Unite.UniteMobileApp.entities.Teams;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;


public interface TeamsRepository extends JpaRepository<Teams, UUID> {
    boolean existsByName(String name);

    @Query("SELECT t FROM Teams t WHERE t.id IN (SELECT tm.teamId FROM TeamMembers tm WHERE tm.userId = :userId)")
    List<Teams> findTeamsByUserId(@Param("userId") UUID userId);

}


