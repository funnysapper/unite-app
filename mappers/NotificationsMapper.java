package com.Unite.UniteMobileApp.mappers;

import com.Unite.UniteMobileApp.dtos.NotificationsDTO;
import com.Unite.UniteMobileApp.dtos.NotificationsUserDto;
import com.Unite.UniteMobileApp.entities.Notifications;
import com.Unite.UniteMobileApp.entities.Users;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
    public class NotificationsMapper {

    public static NotificationsUserDto toNotificationsUserDto(Users user) {
        return new NotificationsUserDto(user.getUserName(), user.getId());
    }

    public static NotificationsDTO.Response toDto(Notifications notification) {
        return NotificationsDTO.Response.builder()
                .user(new NotificationsUserDto(notification.getUserName(), notification.getUserId()))
                .type(notification.getType())
                .message(notification.getMessage())
                .isRead(notification.getIsRead())
                .build();
    }

     public static Notifications toEntity(NotificationsDTO.Request dto) {
        return Notifications.builder()
                .userId(dto.getUser().getId())
                .userName(dto.getUser().getUsername())
                .type(dto.getType())
                .message(dto.getMessage())
                .isRead(dto.getIsRead())
                .build();
    }

}



