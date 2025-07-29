package com.Unite.UniteMobileApp.serviceimpl;

import com.Unite.UniteMobileApp.dtos.MeetingCreateDTO;
import com.Unite.UniteMobileApp.dtos.MeetingResponseDTO;
import com.Unite.UniteMobileApp.dtos.MeetingUpdateDTO;
import com.Unite.UniteMobileApp.dtos.MeetingUpdateRequest;
import com.Unite.UniteMobileApp.entities.Meetings;
import com.Unite.UniteMobileApp.entities.Teams;
import com.Unite.UniteMobileApp.entities.Users;
import com.Unite.UniteMobileApp.mappers.MeetingUpdateMapper;
import com.Unite.UniteMobileApp.miscellaneous.CurrentUserService;
import com.Unite.UniteMobileApp.repositories.MeetingsRepository;
import com.Unite.UniteMobileApp.repositories.TeamMembersRepository;
import com.Unite.UniteMobileApp.repositories.TeamsRepository;
import com.Unite.UniteMobileApp.repositories.UsersRepository;
import com.Unite.UniteMobileApp.services.ActivityServiceInterface;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class MeetingsService {

    @Autowired
    private ActivityServiceInterface activityService;

    @Autowired
    private CurrentUserService currentUserService;

    @Autowired
    private MeetingsRepository meetingsRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private MeetingUpdateMapper meetingUpdateMapper;

    @Autowired
    private TeamsRepository teamsRepository;

    @Autowired
    private TeamMembersRepository teamMembersRepository;

    @Transactional
    public MeetingResponseDTO createMeeting(MeetingCreateDTO dto, String creatorUsername) {
        // Find the creator user
        Users creator = usersRepository.findByUserName(creatorUsername)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Create the Meetings entity and set fields from the DTO
        Meetings meeting = new Meetings();
        meeting.setTitle(dto.getTitle());
        meeting.setStartTime(dto.getStartTime());
        meeting.setEndTime(dto.getEndTime());
        meeting.setMeetingType(dto.getMeetingType());
        meeting.setStatus("SCHEDULED");
        meeting.setCreatedBy(creator);
        meeting.setCreator(creator);

        // Set join code and join link only if requested
        if (Boolean.TRUE.equals(dto.getGenerateJoinCode())) {
            String joinCode = UUID.randomUUID().toString().substring(0, 8);
            meeting.setJoinCode(joinCode);
            meeting.setJoinLink("http://10.30.22.116:9119/join/" + joinCode);
        } else {
            meeting.setJoinCode(null);
            meeting.setJoinLink(null);
        }

        // Set participants
        Set<Users> participants = new HashSet<>();
        if (dto.getParticipantIds() != null) {
            for (UUID userId : dto.getParticipantIds()) {
                Users user = usersRepository.findById(userId)
                        .orElseThrow(() -> new RuntimeException("User not found: " + userId));
                participants.add(user);
            }
        }
        participants.add(creator); // Always add creator as participant
        meeting.setParticipants(participants);

        // Optional team association
        if (dto.getTeamId() != null) {
            Teams team = teamsRepository.findById(dto.getTeamId())
                    .orElseThrow(() -> new RuntimeException("Team not found"));
            if (!teamMembersRepository.existsByTeamIdAndUserId(team.getId(), creator.getId())) {
                throw new RuntimeException("You must be a member of the team to create a meeting for it.");
            }
            meeting.setTeam(team);
        } else {
            meeting.setTeam(null);
        }

        // Save the meeting
        Meetings saved = meetingsRepository.save(meeting);
        MeetingResponseDTO response = toResponseDTO(saved);

        Users currentUser = currentUserService.getCurrentUser();
        activityService.logActivity(currentUser, "meeting_created",
                "Created meeting: " + saved.getTitle());

        return response;

    }

    public List<MeetingResponseDTO> getMeetingsForUser(UUID userId) {
        List<Meetings> meetings = meetingsRepository.findByParticipants_Id(userId);
        return meetings.stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    public MeetingResponseDTO getMeetingById(UUID meetingId) {
        Meetings meeting = meetingsRepository.findById(meetingId)
                .orElseThrow(() -> new RuntimeException("Meeting not found"));
        return toResponseDTO(meeting);
    }

    private MeetingResponseDTO toResponseDTO(Meetings meeting) {
        MeetingResponseDTO dto = new MeetingResponseDTO();
        dto.setId(meeting.getId());
        dto.setTitle(meeting.getTitle());
        dto.setStartTime(meeting.getStartTime());
        dto.setEndTime(meeting.getEndTime());
        dto.setStatus(meeting.getStatus());
        dto.setMeetingType(meeting.getMeetingType());
        dto.setJoinCode(meeting.getJoinCode());
        dto.setJoinLink(meeting.getJoinLink());
        dto.setCreatorId(meeting.getCreator() != null ? meeting.getCreator().getId() : null);
        dto.setParticipantIds(meeting.getParticipants().stream().map(Users::getId).collect(Collectors.toList()));
        dto.setTeamId(meeting.getTeam() != null ? meeting.getTeam().getId() : null);

        return dto;
    }

    public List<MeetingResponseDTO> getMeetingsForUserInRange(UUID userId, LocalDateTime start, LocalDateTime end) {
        List<Meetings> meetings = meetingsRepository.findMeetingsForUserInRange(userId, start, end);
        return meetings.stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    @Transactional
    public MeetingResponseDTO joinMeetingByCode(String joinCode, String username) {
        Meetings meeting = meetingsRepository.findByJoinCode(joinCode)
                .orElseThrow(() -> new RuntimeException("Meeting not found for code: " + joinCode));

        Users user = usersRepository.findByUserName(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        // Add user as participant if not already
        if (!meeting.getParticipants().contains(user)) {
            meeting.getParticipants().add(user);
            meetingsRepository.save(meeting);
        }

        return toResponseDTO(meeting);
    }


    public MeetingResponseDTO updateMeeting(UUID meetingId, MeetingUpdateRequest request) {
        Meetings meeting = meetingsRepository.findById(meetingId)
                .orElseThrow(() -> new RuntimeException("Meeting not found"));

        meeting.setStartTime(request.getStartTime());
        meeting.setEndTime(request.getEndTime());
        meeting.setAgenda(request.getAgenda());
        meeting.setNotes(request.getNotes());

        Meetings savedMeeting = meetingsRepository.save(meeting);
        Users currentUser = currentUserService.getCurrentUser();
        activityService.logActivity(currentUser, "meeting_updated",
                "Updated meeting: " + savedMeeting.getTitle());
        return meetingUpdateMapper.mapToResponseDTO(savedMeeting);
    }

    public void deleteMeeting(UUID meetingId) {
        Meetings meeting = meetingsRepository.findById(meetingId)
                .orElseThrow(() -> new RuntimeException("Meeting not found"));

        String meetingTitle = meeting.getTitle(); // Store title before deletion

        meetingsRepository.delete(meeting);

        // Log activity
        Users currentUser = currentUserService.getCurrentUser();
        activityService.logActivity(currentUser, "meeting_deleted",
                "Deleted meeting: " + meetingTitle);
    }



}



