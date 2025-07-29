package com.Unite.UniteMobileApp.serviceimpl;

import com.Unite.UniteMobileApp.entities.Chat;
import com.Unite.UniteMobileApp.services.MessagesServiceInterface;
import com.Unite.UniteMobileApp.dtos.MessagesDTO;
import com.Unite.UniteMobileApp.entities.Messages;
import com.Unite.UniteMobileApp.entities.Users;
import com.Unite.UniteMobileApp.mappers.MessagesMapper;
import com.Unite.UniteMobileApp.repositories.MessagesRepository;
import com.Unite.UniteMobileApp.repositories.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import com.Unite.UniteMobileApp.repositories.ChatRepository;
@Service
@RequiredArgsConstructor
public class MessagesServiceImpl implements MessagesServiceInterface {

        private final MessagesRepository messagesRepository;
        private final ChatRepository chatRepository;
        private final UsersRepository usersRepository;

        @Override
        public MessagesDTO.Response sendMessage(MessagesDTO.Request requestDto) {
            Chat chat = chatRepository.findById(requestDto.getChatId())
                    .orElseThrow(() -> new RuntimeException("Chat not found"));

            Users sender = usersRepository.findById(requestDto.getSenderId())
                    .orElseThrow(() -> new RuntimeException("Sender not found"));

            Messages message = Messages.builder()
                    .chat(chat)
                    .sender(sender)
                    .content(requestDto.getContent())
                    .messageType(requestDto.getMessageType())
                    .isDeleted(false)
                    .createdAt(LocalDateTime.now())
                    .build();

            Messages saved = messagesRepository.save(message);
            return MessagesMapper.toDto(saved);
        }

        @Override
        public List<MessagesDTO.Response> getMessagesByChatId(UUID chatId) {
            Chat chat = chatRepository.findById(chatId)
                    .orElseThrow(() -> new RuntimeException("Chat not found"));

            return messagesRepository.findByChat(chat).stream()
                    .map(MessagesMapper::toDto)
                    .collect(Collectors.toList());
        }

    @Override
    public void deleteMessage(UUID messageId) {
        if (!messagesRepository.existsById(messageId)) {
            throw new RuntimeException("Message not found with ID: " + messageId);
        }

        messagesRepository.deleteById(messageId);
    }

}




