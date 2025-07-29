package com.Unite.UniteMobileApp.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfileDTO {
    private UUID id;
    private String fullName;
    private String email;
    private String userName;

    public void setUserName(String userName) {
        this.userName = userName != null ? userName.trim() : null;
    }
}