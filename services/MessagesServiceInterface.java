package com.Unite.UniteMobileApp.services;
import com.Unite.UniteMobileApp.dtos.MessagesDTO;
import java.util.List;
import java.util.UUID;

public interface MessagesServiceInterface {

        // Send a new message
        MessagesDTO.Response sendMessage(MessagesDTO.Request requestDto);

        // Get all messages in a chat
        List<MessagesDTO.Response> getMessagesByChatId(UUID chatId);

        // Delete a message by ID
        void deleteMessage(UUID messageId);
    }



