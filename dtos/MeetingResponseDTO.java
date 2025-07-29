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
public class MeetingResponseDTO {
    private UUID id;
    private String title;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;
    private String meetingType;
    private String joinCode;
    private String joinLink;
    private UUID creatorId;
    private List<UUID> participantIds;
    private UUID teamId;

}