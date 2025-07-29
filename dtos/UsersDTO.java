package com.Unite.UniteMobileApp.dtos;

import lombok.*;

import java.util.UUID;


public class UsersDTO {

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        public static class Response {
            private UUID id;
            private String email;
            private String phoneNumber;
            private boolean verified;
            private String userName;
            private String fullName;
            private String regionCode;
            private String token;
        }
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request {
        private String fullName;
        private String email;
        private String phoneNumber;
        private String password;
        private String userName;
        private String regionCode;

        // Custom setter to trim whitespace
        public void setUserName(String userName) {
            this.userName = userName != null ? userName.trim() : null;
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Login {
        private String userName;
        private String password;

        // Custom setter to trim whitespace
        public void setUserName(String userName) {
            this.userName = userName != null ? userName.trim() : null;
        }
    }

}


