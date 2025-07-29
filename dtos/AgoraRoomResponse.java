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
public class AgoraRoomResponse {
    private String roomId;
    private String channelName;
    private String meetingId;
    private UUID creatorId;
    private String creatorName;
    private Long createdAt;
    private Integer participantCount;
    private String status;
}
