package com.Unite.UniteMobileApp.mappers;

import com.Unite.UniteMobileApp.dtos.ChatDTO;
import com.Unite.UniteMobileApp.entities.Chat;
import com.Unite.UniteMobileApp.entities.Teams;
import com.Unite.UniteMobileApp.entities.Meetings;
import com.Unite.UniteMobileApp.entities.Users;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
@Component
    public class ChatMapper {

        public static ChatDTO.Response toDto(Chat chat) {
            return ChatDTO.Response.builder()
                    .id(chat.getId())
                    .isGroup(chat.getIsGroup())
                    .teamId(chat.getTeam() != null ? chat.getTeam().getId() : null)
                    .meetingId(chat.getMeeting() != null ? chat.getMeeting().getId() : null)
                    .createdAt(chat.getCreatedAt())
                    .participants(chat.getParticipants().stream().map(user -> ChatDTO.ParticipantDTO.builder()
                            .userId(user.getId())
                            .name(user.getUserName())
                            .email(user.getEmail())
                            .build()).collect(Collectors.toList()))
                    .build();
        }

        public static Chat toEntity(ChatDTO.Request dto, Teams team, Meetings meeting, List<Users> participants) {
            return Chat.builder()
                    .isGroup(dto.getIsGroup())
                    .team(team)
                    .meeting(meeting)
                    .participants(participants)
                    .build();
        }
    }



