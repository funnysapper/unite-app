package com.Unite.UniteMobileApp.dtos;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

public class MessagesDTO {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request {
        private UUID chatId;
        private UUID senderId;
        private String content;
        private String messageType; // TEXT, FILE, IMAGE, etc.
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private UUID id;
        private UUID chatId;
        private UUID senderId;
        private String senderName;
        private String content;
        private String messageType;
        private boolean isDeleted;
        private LocalDateTime createdAt;
    }
}
