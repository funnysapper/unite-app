package com.Unite.UniteMobileApp.serviceimpl;

import com.Unite.UniteMobileApp.dtos.TeamMembersDTO;
import com.Unite.UniteMobileApp.dtos.TeamsDTO;
import com.Unite.UniteMobileApp.entities.TeamMembers;
import com.Unite.UniteMobileApp.entities.Teams;
import com.Unite.UniteMobileApp.entities.Users;
import com.Unite.UniteMobileApp.mappers.TeamMembersMapper;
import com.Unite.UniteMobileApp.mappers.TeamsMapper;
import com.Unite.UniteMobileApp.repositories.TeamMembersRepository;
import com.Unite.UniteMobileApp.repositories.TeamsRepository;
import com.Unite.UniteMobileApp.repositories.UsersRepository;
import com.Unite.UniteMobileApp.services.TeamsServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeamsServiceImpl implements TeamsServiceInterface {

    private final TeamsRepository teamsRepository;
    private final UsersRepository usersRepository;
    private final TeamMembersRepository teamMembersRepository;

    @Override
    public TeamsDTO.Response createTeam(TeamsDTO.Request requestDto) {
        Users creator = usersRepository.findById(requestDto.getCreatedByUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!creator.getUserName().equals(requestDto.getCreatedByUserName())) {
            throw new RuntimeException("Username does not match the user ID");
        }

        if (teamsRepository.existsByName(requestDto.getName())) {
            throw new RuntimeException("Team with that name already exists");
        }

        Teams team = Teams.builder()
                .name(requestDto.getName())
                .description(requestDto.getDescription())
                .createdById(creator.getId())
                .createdByName(creator.getUserName())
                .build();

        Teams savedTeam = teamsRepository.save(team);

        // Add creator as ADMIN
        teamMembersRepository.save(
                TeamMembers.builder()
                        .teamId(savedTeam.getId())
                        .teamName(savedTeam.getName())
                        .userId(creator.getId())
                        .userName(creator.getUserName())
                        .role("ADMIN")
                        .joinedAt(LocalDateTime.now())
                        .addedByUserId(creator.getId())
                        .addedByUserName(creator.getUserName())
                        .build()
        );

        List<TeamMembersDTO.Response> memberDtos = new ArrayList<>();
        memberDtos.add(new TeamMembersDTO.Response(
                creator.getId(),
                creator.getUserName(),
                "ADMIN"
        ));

        // Add other members as MEMBER
        for (UUID memberId : requestDto.getMemberIds()) {
            if (!memberId.equals(creator.getId())) {
                Users member = usersRepository.findById(memberId)
                        .orElseThrow(() -> new RuntimeException("Member not found"));

                teamMembersRepository.save(
                        TeamMembers.builder()
                                .teamId(savedTeam.getId())
                                .teamName(savedTeam.getName())
                                .userId(member.getId())
                                .userName(member.getUserName())
                                .role("MEMBER")
                                .joinedAt(LocalDateTime.now())
                                .addedByUserName(creator.getUserName())
                                .build()
                );

                memberDtos.add(new TeamMembersDTO.Response(
                        member.getId(),
                        member.getUserName(),
                        "MEMBER"
                ));
            }
        }

        return TeamsMapper.toDto(savedTeam, memberDtos);

    }

    @Override
    public TeamsDTO.Response getTeamById(UUID teamId) {
        Teams team = teamsRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found"));

        List<TeamMembers> members = teamMembersRepository.findByTeamId(teamId);
        List<TeamMembersDTO.Response> memberDtos = new ArrayList<>();
        for (TeamMembers member : members) {
            memberDtos.add(new TeamMembersDTO.Response(
                    member.getUserId(),
                    member.getUserName(),
                    member.getRole()
            ));
        }

        return TeamsMapper.toDto(team, memberDtos);
    }


    @Override
    public TeamsDTO.Response updateTeam(UUID teamId, TeamsDTO.Request requestDto) {
        Teams team = teamsRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found"));

        String oldTeamName = team.getName();
        team.setName(requestDto.getName());
        team.setDescription(requestDto.getDescription());

        Teams updatedTeam = teamsRepository.save(team);

        if (!oldTeamName.equals(updatedTeam.getName()) && !oldTeamName.isBlank()) {
            List<TeamMembers> members = teamMembersRepository.findByTeamId(teamId);
            for (TeamMembers member : members) {
                member.setTeamName(updatedTeam.getName());
            }
            teamMembersRepository.saveAll(members);
        }

        return TeamsMapper.toDto(updatedTeam);
    }

    @Override
    public void deleteTeam(UUID teamId) {
        if (!teamsRepository.existsById(teamId)) {
            throw new RuntimeException("Team not found");
        }
        teamsRepository.deleteById(teamId);
    }

    @Override
    public List<TeamsDTO.Response> getTeamsByUserId(UUID userId) {
        // First, get all teams where this user is a member
        List<Teams> teams = teamsRepository.findTeamsByUserId(userId);

        // Fetch all team members for these teams
        List<TeamMembers> allMembers = teamMembersRepository.findByUserId(userId);

        // Map teamId to members (optional)
        Map<UUID, List<TeamMembersDTO.Response>> teamMembersMap = new HashMap<>();
        for (TeamMembers tm : allMembers) {
            teamMembersMap
                    .computeIfAbsent(tm.getTeamId(), k -> new ArrayList<>())
                    .add(TeamMembersMapper.toDto(tm));
        }

        // Build the response
        return teams.stream().map(team -> {
            List<TeamMembersDTO.Response> memberDtos = teamMembersMap.getOrDefault(team.getId(), new ArrayList<>());
            Users creator = usersRepository.findById(team.getCreatedById()).orElse(null);
            String creatorName = creator != null ? creator.getUserName() : "Unknown";
            return TeamsMapper.toDto(team, creatorName, memberDtos);
        }).collect(Collectors.toList());
    }

    public List<TeamsDTO.Response> getAllTeams() {
        List<Teams> teams = teamsRepository.findAll();
        return teams.stream()
                .map(team -> new TeamsDTO.Response(team.getId(), team.getName(), team.getDescription()))
                .collect(Collectors.toList());
    }

}
