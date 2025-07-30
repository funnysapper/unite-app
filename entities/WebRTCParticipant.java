package com.Unite.UniteMobileApp.entities;



public class WebRTCParticipant {
    private String userId;
    private String username;

    public WebRTCParticipant(String userId, String username) {
        this.userId = userId;
        this.username = username;
    }

    // getters and setters
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
}