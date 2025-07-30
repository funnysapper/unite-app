package com.Unite.UniteMobileApp.controllers;

import com.Unite.UniteMobileApp.dtos.ChatDTO;
import com.Unite.UniteMobileApp.entities.Chat;
import com.Unite.UniteMobileApp.services.ChatServiceInterface;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
    @RequestMapping("/api/chats")
    public class ChatController {

        private final ChatServiceInterface chatService;

        public ChatController(ChatServiceInterface chatService) {
            this.chatService = chatService;
        }

        @PostMapping
        public ResponseEntity<ChatDTO.Response> createChat(@RequestBody ChatDTO.Request request) {
            ChatDTO.Response response = chatService.createChat(request);
            return ResponseEntity.ok(response);
        }

        @GetMapping("/{id}")
        public ResponseEntity<ChatDTO.Response> getChatById(@PathVariable UUID id) {
            return ResponseEntity.ok(chatService.getChatById(id));
        }

        @GetMapping
        public ResponseEntity<List<ChatDTO.Response>> getAllChats() {
            return ResponseEntity.ok(chatService.getAllChats());
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteChat(@PathVariable UUID id) {
            chatService.deleteChat(id);
            return ResponseEntity.noContent().build();
        }

        @GetMapping("/direct")
        public ResponseEntity<ChatDTO.Response> getOrCreateDirectChat(
                @RequestParam UUID userId1,
                @RequestParam UUID userId2) {
            ChatDTO.Response chat = chatService.getOrCreateChatBetweenUsers(userId1, userId2);
            return ResponseEntity.ok(chat);
        }

        @GetMapping("/meeting/{meetingId}")
        public ResponseEntity<ChatDTO.Response> getChatByMeeting(@PathVariable UUID meetingId) {
            return ResponseEntity.ok(chatService.getChatByMeetingId(meetingId));
        }

        @GetMapping("/team/{teamId}")
        public ResponseEntity<List<ChatDTO.Response>> getChatsByTeam(@PathVariable UUID teamId) {
            List<ChatDTO.Response> chats = chatService.getChatsByTeam(teamId);
            return ResponseEntity.ok(chats);
        }

        @GetMapping("/groups")
        public ResponseEntity<List<ChatDTO.Response>> getGroupChats() {
            List<ChatDTO.Response> chats = chatService.getGroupChats();
            return ResponseEntity.ok(chats);
        }
    }


