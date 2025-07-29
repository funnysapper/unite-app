package com.Unite.UniteMobileApp.repositories;

import com.Unite.UniteMobileApp.entities.TeamMembers;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TeamMembersRepository extends JpaRepository<TeamMembers, Long> {

    List<TeamMembers> findByTeamId(UUID teamId);

    boolean existsByTeamIdAndUserId(UUID teamId, UUID userId);

    List<TeamMembers> findByUserId(UUID userId);

    void deleteById(@NonNull UUID id);

    List<TeamMembers> findByAddedByUserId(UUID addedByUserId);

    TeamMembers findByTeamIdAndUserId(UUID teamId, UUID id);

    Optional<TeamMembers> findById(UUID id);

    boolean existsById(@NonNull UUID id);

    boolean existsByTeamIdAndUserIdAndRole(UUID teamId, UUID userId, String role);

}








