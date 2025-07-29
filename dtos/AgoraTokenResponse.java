package com.Unite.UniteMobileApp.dtos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AgoraTokenResponse {
    private String token;
    private String channelName;
    private String uid;
    private String role;
    private String appId;
}