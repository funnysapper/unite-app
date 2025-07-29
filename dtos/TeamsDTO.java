package com.Unite.UniteMobileApp.dtos;

import lombok.*;

import java.util.List;
import java.util.UUID;

public class TeamsDTO {

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        public static class Response {
            private UUID teamId;
            private String name;
            private String description;
            private String createdByName;
            private List<TeamMembersDTO.Response> members;


            public Response(UUID teamId, String name, String description) {
                this.teamId = teamId;
                this.name= name;
                this.description = description;
            }
        }

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        public static class Request {
            private String name;
            private String description;
            private UUID createdByUserId;
            private String createdByUserName;
            private List<UUID> memberIds;

        }
    }


