package com.Unite.UniteMobileApp.mappers;

import com.Unite.UniteMobileApp.dtos.TeamMembersDTO;
import com.Unite.UniteMobileApp.entities.TeamMembers;
import com.Unite.UniteMobileApp.entities.Users;
import com.Unite.UniteMobileApp.entities.Teams;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TeamMembersMapper {

    public static TeamMembersDTO.Response toDto(TeamMembers member) {
        return TeamMembersDTO.Response.builder()
                .id(member.getUserId())
                .name(member.getUserName())
                .role(member.getRole())
                .build();
    }

    public static TeamMembers toEntity(TeamMembersDTO.Request dto, String userName,
                                       String teamName) {
        return TeamMembers.builder()
                .teamId(dto.getTeamId())
                .teamName(teamName)
                .userId(dto.getUserId())
                .userName(userName)
                .role(dto.getRole())
                .addedByUserId(dto.getAddedByUserId())
                .addedByUserName(dto.getAddedByUsername())
                .build();
    }
}

