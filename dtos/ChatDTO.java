package com.Unite.UniteMobileApp.dtos;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class ChatDTO {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request {
        private Boolean isGroup;
        private UUID teamId;     // required
        private UUID meetingId;  // required
        private List<UUID> participantIds;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private UUID id;
        private Boolean isGroup;
        private UUID teamId;
        private UUID meetingId;
        private LocalDateTime createdAt;
        private List<ParticipantDTO> participants;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ParticipantDTO {
        private UUID userId;
        private String name;
        private String email;
    }
}
