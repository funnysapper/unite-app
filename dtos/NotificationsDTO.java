package com.Unite.UniteMobileApp.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

public class NotificationsDTO {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request {
        private NotificationsUserDto user;
        private String type;
        private String message;
        private Boolean isRead = false;


    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private NotificationsUserDto user;
        private String type;
        private String message;
        private Boolean isRead = false;


    }
}
