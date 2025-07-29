package com.Unite.UniteMobileApp.serviceimpl;

import com.Unite.UniteMobileApp.dtos.NotificationsDTO;
import com.Unite.UniteMobileApp.dtos.NotificationsUserDto;
import com.Unite.UniteMobileApp.entities.Notifications;
import com.Unite.UniteMobileApp.entities.Users;
import com.Unite.UniteMobileApp.exceptions.UserNotFoundException;
import com.Unite.UniteMobileApp.mappers.NotificationsMapper;
import com.Unite.UniteMobileApp.repositories.NotificationsRepository;
import com.Unite.UniteMobileApp.repositories.UsersRepository;
import com.Unite.UniteMobileApp.services.NotificationsServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

    @Service
    @RequiredArgsConstructor
    public class NotificationsServiceImpl implements NotificationsServiceInterface {

        private final NotificationsRepository repository;
        private final UsersRepository usersRepository;

        @Override
        public NotificationsDTO.Response createNotification(NotificationsDTO.Request request) {
            Users user = usersRepository.findById(request.getUser().getId())
                    .orElseThrow(() -> new UserNotFoundException("User not found"));

            Notifications notification = NotificationsMapper.toEntity(request);
            Notifications saved = repository.save(notification);
            return NotificationsMapper.toDto(saved);
        }


        @Override
        public List<NotificationsDTO.Response> getAllNotifications() {
            return repository.findAll().stream()
                    .map(NotificationsMapper::toDto)
                    .collect(Collectors.toList());
        }

        @Override
        public List<NotificationsDTO.Response> getNotificationsByUserId(UUID userId) {
            return repository.findByUserId(userId).stream()
                    .map(NotificationsMapper::toDto)
                    .collect(Collectors.toList());
        }


        @Override
        public void markAsRead(UUID notificationId) {
            Notifications notification = repository.findById(notificationId)
                    .orElseThrow(() -> new RuntimeException("Notification not found with id: " + notificationId));
            notification.setIsRead(true);
            repository.save(notification);
        }

        @Override
        public void deleteNotification(UUID notificationId) {
            if (!repository.existsById(notificationId)) {
                throw new RuntimeException("Notification not found with id: " + notificationId);
            }
            repository.deleteById(notificationId);
        }
    }


