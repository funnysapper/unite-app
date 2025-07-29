package com.Unite.UniteMobileApp.mappers;

import com.Unite.UniteMobileApp.dtos.TeamsDTO;
import com.Unite.UniteMobileApp.dtos.TeamMembersDTO;
import com.Unite.UniteMobileApp.entities.Teams;
import com.Unite.UniteMobileApp.entities.Users;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TeamsMapper {

    // Convert request DTO + creator into Teams entity
    public static Teams toEntity(TeamsDTO.Request dto, Users creator) {
        return Teams.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .createdById(creator.getId())
                .createdByName(creator.getUserName())
                .build();
    }

    // Convert Teams entity to Response DTO using relationships
    public static TeamsDTO.Response toDto(Teams team, List<TeamMembersDTO.Response> members) {
        return TeamsDTO.Response.builder()
                .teamId(team.getId())
                .name(team.getName())
                .description(team.getDescription())
                .createdByName(team.getCreatedByName())
                .members(members)
                .build();
    }

    // Overload for basic TeamsDTO without members
    public static TeamsDTO.Response toDto(Teams team) {
        return TeamsDTO.Response.builder()
                .teamId(team.getId())
                .name(team.getName())
                .description(team.getDescription())
                .createdByName(team.getCreatedByName()) // ensure this is always included
                .build();
    }

    public static TeamsDTO.Response toDto(Teams team, String createdByName, List<TeamMembersDTO.Response> members) {
        return TeamsDTO.Response.builder()
                .teamId(team.getId())
                .name(team.getName())
                .description(team.getDescription())
                .createdByName(createdByName)
                .members(members)
                .build();
    }

}
