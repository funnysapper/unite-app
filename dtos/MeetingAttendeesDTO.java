package com.Unite.UniteMobileApp.dtos;
import com.Unite.UniteMobileApp.entities.Meetings;
import com.Unite.UniteMobileApp.entities.Users;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;


public class MeetingAttendeesDTO {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder

    public static class Response {
        private UUID id;
        private Meetings meeting;
        private Users user;
        private LocalDateTime joinedAt;
        private LocalDateTime leftAt;
        private String role;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request {
        private UUID id;
        private Meetings meeting;
        private Users user;
        private LocalDateTime joinedAt;
        private LocalDateTime leftAt;
        private String role;
    }

}
