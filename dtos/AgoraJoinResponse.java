package com.Unite.UniteMobileApp.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AgoraJoinResponse {
    private AgoraRoomResponse roomInfo;
    private AgoraUserInfo userInfo;
    private String token;
    private String appId;
}
