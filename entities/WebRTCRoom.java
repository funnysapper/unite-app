package com.Unite.UniteMobileApp.entities;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WebRTCRoom {
    private String roomId;
    private Map<String, WebRTCParticipant> participants = new ConcurrentHashMap<>();

    public WebRTCRoom(String roomId) {
        this.roomId = roomId;
    }

    public void addParticipant(WebRTCParticipant participant) {
        participants.put(participant.getUserId(), participant);
    }

    public WebRTCParticipant removeParticipant(String userId) {
        return participants.remove(userId);
    }

    public Collection<WebRTCParticipant> getParticipants() {
        return participants.values();
    }

    public String getRoomId() { return roomId; }
    public void setRoomId(String roomId) { this.roomId = roomId; }
}