package com.Unite.UniteMobileApp.services;

import com.Unite.UniteMobileApp.entities.Users;
import java.util.Map;

public interface AgoraServiceInterface {

    /**
     * Generate Agora token for a user to join a channel
     * @param channelName The channel/meeting name
     * @param uid The user ID
     * @param role The user role (publisher or subscriber)
     * @return Generated token
     */
    String generateToken(String channelName, String uid, String role);

    /**
     * Create a video call room
     * @param meetingId The meeting ID
     * @param channelName The channel name
     * @param creator The meeting creator
     * @return Room information
     */
    Map<String, Object> createVideoRoom(String meetingId, String channelName, Users creator);

    /**
     * Join a video call room
     * @param channelName The channel name
     * @param user The user joining
     * @return Join information
     */
    Map<String, Object> joinVideoRoom(String channelName, Users user);

    /**
     * Leave a video call room
     * @param channelName The channel name
     * @param user The user leaving
     */
    void leaveVideoRoom(String channelName, Users user);

    /**
     * Get room participants
     * @param channelName The channel name
     * @return List of participants
     */
    Map<String, Object> getRoomParticipants(String channelName);

    /**
     * Validate Agora token
     * @param token The token to validate
     * @param channelName The channel name
     * @param uid The user ID
     * @return True if valid
     */
    boolean validateToken(String token, String channelName, String uid);
}