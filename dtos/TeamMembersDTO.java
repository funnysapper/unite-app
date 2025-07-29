package com.Unite.UniteMobileApp.dtos;

import lombok.*;

import java.util.UUID;

public class TeamMembersDTO {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private UUID id;
        private String name;
        private String role;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request {
        private UUID teamId;
        private UUID userId;
        private UUID addedByUserId;
        private String addedByUsername;
        private String role;
    }

}
