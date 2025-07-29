package com.Unite.UniteMobileApp.controllers;

import com.Unite.UniteMobileApp.dtos.MessagesDTO;
import com.Unite.UniteMobileApp.services.MessagesServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessagesController {

    private final MessagesServiceInterface messagesService;

    // SEND a message
    @PostMapping
    public ResponseEntity<MessagesDTO.Response> sendMessage(@RequestBody MessagesDTO.Request requestDto) {
        MessagesDTO.Response response = messagesService.sendMessage(requestDto);
        return ResponseEntity.ok(response);
    }

    // GET all messages in a chat
    @GetMapping("/chat/{chatId}")
    public ResponseEntity<List<MessagesDTO.Response>> getMessagesByChatId(@PathVariable UUID chatId) {
        List<MessagesDTO.Response> messages = messagesService.getMessagesByChatId(chatId);
        return ResponseEntity.ok(messages);
    }

    // DELETE a message
    @DeleteMapping("/{messageId}")
    public ResponseEntity<Void> deleteMessage(@PathVariable UUID messageId) {
        messagesService.deleteMessage(messageId);
        return ResponseEntity.noContent().build();
    }
}
