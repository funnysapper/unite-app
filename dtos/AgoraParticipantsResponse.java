package com.Unite.UniteMobileApp.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AgoraParticipantsResponse {
    private String channelName;
    private Map<String, AgoraUserInfo> participants;
    private Integer participantCount;
}
