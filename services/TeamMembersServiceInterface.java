package com.Unite.UniteMobileApp.services;

import com.Unite.UniteMobileApp.dtos.TeamMembersDTO;
import com.Unite.UniteMobileApp.dtos.TeamsDTO;

import java.util.List;
import java.util.UUID;

public interface TeamMembersServiceInterface {

        TeamMembersDTO.Response addTeamMember(TeamMembersDTO.Request request);

        List<TeamMembersDTO.Response> getAllTeamMembers();

        TeamMembersDTO.Response getTeamMemberById(UUID id);

        TeamMembersDTO.Response updateTeamMember(UUID id, TeamsDTO.Request request);

        void deleteTeamMember(UUID id);
    }


