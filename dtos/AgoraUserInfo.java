package com.Unite.UniteMobileApp.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AgoraUserInfo {
    private UUID userId;
    private String userName;
    private String fullName;
    private Long joinedAt;
    private String role;
}
