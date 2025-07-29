package com.Unite.UniteMobileApp.services;
import com.Unite.UniteMobileApp.dtos.TeamsDTO;

import java.util.List;
import java.util.UUID;

public interface TeamsServiceInterface {

        // Create a new team
        TeamsDTO.Response createTeam(TeamsDTO.Request requestDto);

        // Get a team by its ID
        TeamsDTO.Response getTeamById(UUID teamId);

        // Update team details
        TeamsDTO.Response updateTeam(UUID teamId, TeamsDTO.Request requestDto);

        List<TeamsDTO.Response> getTeamsByUserId(UUID userId);


        // Delete a team
        void deleteTeam(UUID teamId);

        List<TeamsDTO.Response> getAllTeams();
}


