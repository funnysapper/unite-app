package com.Unite.UniteMobileApp.services;

import com.Unite.UniteMobileApp.dtos.NotificationsDTO;
import java.util.List;
import java.util.UUID;

public interface NotificationsServiceInterface {

        NotificationsDTO.Response createNotification(NotificationsDTO.Request request);

        List<NotificationsDTO.Response> getAllNotifications();

        List<NotificationsDTO.Response> getNotificationsByUserId(UUID userId);

        void markAsRead(UUID notificationId);

        void deleteNotification(UUID notificationId);
    }


