package com.Unite.UniteMobileApp.serviceimpl;

import com.Unite.UniteMobileApp.dtos.TeamMembersDTO;
import com.Unite.UniteMobileApp.dtos.TeamsDTO;
import com.Unite.UniteMobileApp.entities.TeamMembers;
import com.Unite.UniteMobileApp.entities.Teams;
import com.Unite.UniteMobileApp.entities.Users;
import com.Unite.UniteMobileApp.repositories.TeamMembersRepository;
import com.Unite.UniteMobileApp.repositories.TeamsRepository;
import com.Unite.UniteMobileApp.repositories.UsersRepository;
import com.Unite.UniteMobileApp.services.TeamMembersServiceInterface;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeamMembersServiceImpl implements TeamMembersServiceInterface {

    private final TeamMembersRepository teamMembersRepository;
    private final UsersRepository usersRepository;
    private final TeamsRepository teamsRepository;

    @Override
    public TeamMembersDTO.Response addTeamMember(TeamMembersDTO.Request request) {
        if (teamMembersRepository.existsByTeamIdAndUserId(request.getTeamId(), request.getUserId())) {
            throw new RuntimeException("User already in the team");
        }

        Users user = usersRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Users addedBy = usersRepository.findById(request.getAddedByUserId())
                .orElseThrow(() -> new RuntimeException("Added-by user not found"));

        TeamMembers addedByMember = teamMembersRepository.findByTeamIdAndUserId(request.getTeamId(), addedBy.getId());
        if (addedByMember == null || !addedByMember.getRole().equalsIgnoreCase("ADMIN")) {
            throw new RuntimeException("Only team admins can add members");
        }

        Teams team = teamsRepository.findById(request.getTeamId())
                .orElseThrow(() -> new RuntimeException("Team not found"));

        TeamMembers member = TeamMembers.builder()
                .userId(user.getId())
                .userName(user.getUserName())
                .teamId(team.getId())
                .teamName(team.getName())
                .role("MEMBER")
                .joinedAt(LocalDateTime.now())
                .addedByUserId(addedBy.getId())
                .addedByUserName(addedBy.getUserName())
                .build();

        TeamMembers saved = teamMembersRepository.save(member);

        return TeamMembersDTO.Response.builder()
                .id(saved.getId())
                .name(saved.getUserName())
                .role(saved.getRole())
                .build();

    }

    @Override
    public List<TeamMembersDTO.Response> getAllTeamMembers() {
        return teamMembersRepository.findAll().stream().map(member ->
                TeamMembersDTO.Response.builder()
                        .id(member.getId())
                        .name(member.getUserName())
                        .role(member.getRole())
                        .build()
        ).collect(Collectors.toList());
    }

    @Override
    public TeamMembersDTO.Response getTeamMemberById(UUID id) {
        TeamMembers member = teamMembersRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Team member not found"));

        return TeamMembersDTO.Response.builder()
                .id(member.getId())
                .name(member.getUserName())
                .role(member.getRole())
                .build();
    }

    @Override
    public TeamMembersDTO.Response updateTeamMember(UUID id, TeamsDTO.Request request) {
        TeamMembers member = teamMembersRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Team member not found"));

        member.setRole("MEMBER");
        member.setJoinedAt(LocalDateTime.now());

        TeamMembers updated = teamMembersRepository.save(member);

        return TeamMembersDTO.Response.builder()
                .id(updated.getId())
                .name(updated.getUserName())
                .role(updated.getRole())
                .build();
    }

    @Override
    public void deleteTeamMember(@NonNull UUID id) {
        if (!teamMembersRepository.existsById(id)) {
            throw new RuntimeException("Team member not found");
        }
        teamMembersRepository.deleteById(id);
    }
}
