package com.Unite.UniteMobileApp.controllers;

import com.Unite.UniteMobileApp.dtos.NotificationsDTO;
import com.Unite.UniteMobileApp.services.NotificationsServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
    @RequestMapping("/api/notifications")
    @RequiredArgsConstructor
    public class NotificationsController {

        private final NotificationsServiceInterface notificationsService;

        // CREATE a new notification
        @PostMapping
        public ResponseEntity<NotificationsDTO.Response> createNotification(@RequestBody NotificationsDTO.Request request) {
            NotificationsDTO.Response response = notificationsService.createNotification(request);
            return ResponseEntity.ok(response);
        }

        // GET all notifications
        @GetMapping
        public ResponseEntity<List<NotificationsDTO.Response>> getAllNotifications() {
            List<NotificationsDTO.Response> responseList = notificationsService.getAllNotifications();
            return ResponseEntity.ok(responseList);
        }

        // GET notifications for a specific user
        @GetMapping("/user/{userId}")
        public ResponseEntity<List<NotificationsDTO.Response>> getNotificationsByUserId(@PathVariable UUID userId) {
            List<NotificationsDTO.Response> userNotifications = notificationsService.getNotificationsByUserId(userId);
            return ResponseEntity.ok(userNotifications);
        }

        // MARK a notification as read
        @PutMapping("/{notificationId}/mark-as-read")
        public ResponseEntity<Void> markAsRead(@PathVariable UUID notificationId) {
            notificationsService.markAsRead(notificationId);
            return ResponseEntity.noContent().build();
        }

        // DELETE a notification
        @DeleteMapping("/{notificationId}")
        public ResponseEntity<Void> deleteNotification(@PathVariable UUID notificationId) {
            notificationsService.deleteNotification(notificationId);
            return ResponseEntity.noContent().build();
        }
    }


