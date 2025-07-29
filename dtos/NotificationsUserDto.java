package com.Unite.UniteMobileApp.dtos;

import java.util.UUID;

public class NotificationsUserDto {
    private String username;
    private UUID id;

    public NotificationsUserDto(String username, UUID id){
        this.username = username;
        this.id = id;
    }

    public void setUserName(String username) {
        this.username = username != null ? username.trim() : null;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUsername(){
        return username;
    }

    public UUID getId(){
        return id;
    }
}
