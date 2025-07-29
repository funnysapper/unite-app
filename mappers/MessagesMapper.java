package com.Unite.UniteMobileApp.mappers;

import com.Unite.UniteMobileApp.dtos.MessagesDTO;
import com.Unite.UniteMobileApp.entities.Chat;
import com.Unite.UniteMobileApp.entities.Messages;
import com.Unite.UniteMobileApp.entities.Users;
import org.springframework.stereotype.Component;

@Component
    public class MessagesMapper {

        // Convert from entity to response DTO
        public static MessagesDTO.Response toDto(Messages message) {
            Users sender = message.getSender();

            return MessagesDTO.Response.builder()
                    .id(message.getId())
                    .chatId(message.getChat().getId())
                    .senderId(sender.getId())
                    .senderName(sender.getUserName())
                    .content(message.getContent())
                    .messageType(message.getMessageType())
                    .isDeleted(message.isDeleted())
                    .createdAt(message.getCreatedAt())
                    .build();
        }

        // Convert from request DTO to entity (requires Chat and Sender objects)
        public static Messages toEntity(MessagesDTO.Request dto, Users sender, Chat chat) {
            return Messages.builder()
                    .chat(chat)
                    .sender(sender)
                    .content(dto.getContent())
                    .messageType(dto.getMessageType())
                    .isDeleted(false) // default on creation
                    .build();
        }
    }


