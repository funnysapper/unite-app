package com.Unite.UniteMobileApp.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MeetingCreateDTO {
    private String title;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String meetingType; // "VIDEO" or "AUDIO"
    private List<UUID> participantIds; // UUIDs of users to invite
    private UUID teamId;// optional
    private Boolean generateJoinCode;

}