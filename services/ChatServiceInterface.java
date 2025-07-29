package com.Unite.UniteMobileApp.services;

import com.Unite.UniteMobileApp.dtos.ChatDTO;
import java.util.List;
import java.util.UUID;

public interface ChatServiceInterface {

        ChatDTO.Response createChat(ChatDTO.Request request);

        ChatDTO.Response getChatById(UUID id);

        List<ChatDTO.Response> getAllChats();

       ChatDTO.Response getOrCreateChatBetweenUsers(UUID userId1, UUID userId2);

       void deleteChat(UUID id);

       ChatDTO.Response getChatByMeetingId(UUID meetingId);

    List<ChatDTO.Response> getGroupChats();

    List<ChatDTO.Response> getChatsByTeam(UUID teamId);
}


