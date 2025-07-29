package com.Unite.UniteMobileApp.controllers;

import com.Unite.UniteMobileApp.dtos.TeamMembersDTO;
import com.Unite.UniteMobileApp.dtos.TeamsDTO;
import com.Unite.UniteMobileApp.services.TeamMembersServiceInterface;
import com.Unite.UniteMobileApp.services.TeamsServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
    @RequestMapping("/api/team-members")
    @RequiredArgsConstructor
    public class TeamMembersController {

        private final TeamMembersServiceInterface teamMembersService;
        @Autowired
        private TeamsServiceInterface teamsService;


        // CREATE a team member
        @PostMapping
        public ResponseEntity<TeamMembersDTO.Response> createTeamMember(@RequestBody TeamMembersDTO.Request request) {
            TeamMembersDTO.Response response = teamMembersService.addTeamMember(request);
            return ResponseEntity.ok(response);
        }

        // GET all team members
        @GetMapping
        public ResponseEntity<List<TeamMembersDTO.Response>> getAllTeamMembers() {
            List<TeamMembersDTO.Response> responseList = teamMembersService.getAllTeamMembers();
            return ResponseEntity.ok(responseList);
        }

        // GET a single team member by ID
        @GetMapping("/{id}")
        public ResponseEntity<TeamMembersDTO.Response> getTeamMemberById(@PathVariable UUID id) {
            TeamMembersDTO.Response response = teamMembersService.getTeamMemberById(id);
            return ResponseEntity.ok(response);
        }

        // UPDATE a team member
        @PutMapping("/{id}")
        public ResponseEntity<TeamMembersDTO.Response> updateTeamMember(
                @PathVariable UUID id,
                @RequestBody TeamsDTO.Request request
        ) {
            TeamMembersDTO.Response response = teamMembersService.updateTeamMember(id, request);
            return ResponseEntity.ok(response);
        }

        // DELETE a team member
        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteTeamMember(@PathVariable UUID id) {
            teamMembersService.deleteTeamMember(id);
            return ResponseEntity.noContent().build();
        }
    }


