package com.Unite.UniteMobileApp.mappers;

import com.Unite.UniteMobileApp.dtos.MeetingResponseDTO;
import com.Unite.UniteMobileApp.entities.Meetings;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class MeetingUpdateMapper {

    public MeetingResponseDTO mapToResponseDTO(Meetings meeting) {
        return MeetingResponseDTO.builder()
                .id(meeting.getId())
                .title(meeting.getTitle())
                .startTime(meeting.getStartTime())
                .endTime(meeting.getEndTime())
                .status(meeting.getStatus())
                .meetingType(meeting.getMeetingType())
                .joinCode(meeting.getJoinCode())
                .joinLink(meeting.getJoinLink())
                .creatorId(meeting.getCreatedBy() != null ? meeting.getCreatedBy().getId() : null)
                .participantIds(meeting.getParticipants() != null ?
                        meeting.getParticipants().stream()
                                .map(participant -> participant.getId())
                                .collect(Collectors.toList()) : null)
                .teamId(meeting.getTeam() != null ? meeting.getTeam().getId() : null)
                .build();
    }
}