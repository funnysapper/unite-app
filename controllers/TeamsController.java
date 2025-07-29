package com.Unite.UniteMobileApp.controllers;

import com.Unite.UniteMobileApp.miscellaneous.SimpleMessage;
import com.Unite.UniteMobileApp.dtos.TeamsDTO;
import com.Unite.UniteMobileApp.services.TeamsServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
    @RequestMapping("/api/teams")
    @RequiredArgsConstructor
    public class TeamsController {

        private final TeamsServiceInterface teamsService;

        //  Create new team
        @PostMapping
        public ResponseEntity<SimpleMessage> createTeam(@RequestBody TeamsDTO.Request requestDto) {
            TeamsDTO.Response newTeam = teamsService.createTeam(requestDto);
            return ResponseEntity.ok(new SimpleMessage("Team created!"));
        }

        //  Get team by ID
        @GetMapping("/{id}")
        public ResponseEntity<TeamsDTO.Response> getTeamById(@PathVariable("id") UUID teamId) {
            TeamsDTO.Response team = teamsService.getTeamById(teamId);
            return ResponseEntity.ok(team);
        }

        //  Update team
        @PutMapping("/{id}")
        public ResponseEntity<SimpleMessage> updateTeam(
                @PathVariable("id") UUID teamId,
                @RequestBody TeamsDTO.Request requestDto) {
            TeamsDTO.Response updatedTeam = teamsService.updateTeam(teamId, requestDto);
            return ResponseEntity.ok(new SimpleMessage("Team updated!"));
        }

        //  Delete team
        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteTeam(@PathVariable("id") UUID teamId) {
            teamsService.deleteTeam(teamId);
            return ResponseEntity.noContent().build();
        }

    // Get all teams (optionally, for the current user)
    @GetMapping
    public ResponseEntity<List<TeamsDTO.Response>> getAllTeams() {
        List<TeamsDTO.Response> teams = teamsService.getAllTeams();
        return ResponseEntity.ok(teams);
    }
}


