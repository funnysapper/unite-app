package com.Unite.UniteMobileApp.serviceimpl;

import com.Unite.UniteMobileApp.dtos.ChatDTO;
import com.Unite.UniteMobileApp.entities.Chat;
import com.Unite.UniteMobileApp.entities.Meetings;
import com.Unite.UniteMobileApp.entities.Teams;
import com.Unite.UniteMobileApp.entities.Users;
import com.Unite.UniteMobileApp.mappers.ChatMapper;
import com.Unite.UniteMobileApp.repositories.ChatRepository;
import com.Unite.UniteMobileApp.repositories.MeetingsRepository;
import com.Unite.UniteMobileApp.repositories.TeamsRepository;
import com.Unite.UniteMobileApp.repositories.UsersRepository;
import com.Unite.UniteMobileApp.services.ChatServiceInterface;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ChatServiceImpl implements ChatServiceInterface {

    private final ChatRepository chatRepository;
    private final TeamsRepository teamRepository;
    private final MeetingsRepository meetingRepository;
    private final UsersRepository userRepository;

    public ChatServiceImpl(ChatRepository chatRepository,
                           TeamsRepository teamRepository,
                           MeetingsRepository meetingRepository,
                           UsersRepository userRepository,
                           ChatMapper chatMapper) {
        this.chatRepository = chatRepository;
        this.teamRepository = teamRepository;
        this.meetingRepository = meetingRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ChatDTO.Response createChat(ChatDTO.Request request) {
        Teams team = null;
        if (request.getTeamId() != null) {
            team = teamRepository.findById(request.getTeamId())
                    .orElseThrow(() -> new RuntimeException("Team not found with id: " + request.getTeamId()));
        }

        Meetings meeting = null;
        if (request.getMeetingId() != null) {
            meeting = meetingRepository.findById(request.getMeetingId())
                    .orElseThrow(() -> new RuntimeException("Meeting not found with id: " + request.getMeetingId()));
        }

        List<Users> participants = userRepository.findAllById(request.getParticipantIds());

        Chat chat = ChatMapper.toEntity(request, team, meeting, participants);
        Chat savedChat = chatRepository.save(chat);

        return ChatMapper.toDto(savedChat);
    }

    @Override
    public ChatDTO.Response getChatById(UUID id) {
        Chat chat = chatRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Chat not found with id: " + id));
        return ChatMapper.toDto(chat);
    }

    @Override
    public List<ChatDTO.Response> getAllChats() {
        List<Chat> chats = chatRepository.findAll();
        return chats.stream()
                .map(ChatMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteChat(UUID id) {
        if (!chatRepository.existsById(id)) {
            throw new RuntimeException("Chat not found with id: " + id);
        }
        chatRepository.deleteById(id);
    }

    @Override
    public ChatDTO.Response getOrCreateChatBetweenUsers(UUID userId1, UUID userId2) {
        Users user1 = userRepository.findById(userId1)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId1));
        Users user2 = userRepository.findById(userId2)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId2));

        // Check if a chat already exists with only these two participants and no team/meeting
        Optional<Chat> existingChat = chatRepository.findDirectChatBetweenUsers(userId1, userId2);

        if (existingChat.isPresent()) {
            return ChatMapper.toDto(existingChat.get());
        }

        // Create new chat
        Chat chat = Chat.builder()
                .participants(List.of(user1, user2))
                .team(null)
                .meeting(null)
                .build();

        Chat saved = chatRepository.save(chat);
        return ChatMapper.toDto(saved);
    }

    @Override
    public ChatDTO.Response getChatByMeetingId(UUID meetingId) {
        Meetings meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new RuntimeException("Meeting not found"));

        List<Chat> chats = chatRepository.findByMeeting(meeting);
        if (chats.isEmpty()) {
            throw new RuntimeException("No chat found for this meeting");
        }

        return ChatMapper.toDto(chats.get(0)); // Assuming 1 chat per meeting
    }

    @Override
    public List<ChatDTO.Response> getGroupChats() {
        List<Chat> groupChats = chatRepository.findByIsGroupTrue();

        return groupChats.stream()
                .map(ChatMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ChatDTO.Response> getChatsByTeam(UUID teamId) {
        Teams team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found with id: " + teamId));

        List<Chat> chats = chatRepository.findByTeam(team);

        return chats.stream()
                .map(ChatMapper::toDto)
                .collect(Collectors.toList());
    }


}
