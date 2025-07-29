package com.Unite.UniteMobileApp.serviceimpl;

import com.Unite.UniteMobileApp.entities.Users;
import com.Unite.UniteMobileApp.services.AgoraServiceInterface;
import com.Unite.UniteMobileApp.miscellaneous.CurrentUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.UUID;

@Service
public class AgoraServiceImpl implements AgoraServiceInterface {

    private static final Logger logger = LoggerFactory.getLogger(AgoraServiceImpl.class);

    @Autowired
    private CurrentUserService currentUserService; // Uses your existing user service

    // Agora configuration - these should be in application.properties
    @Value("${agora.app.id:YOUR_AGORA_APP_ID}")
    private String agoraAppId;

    @Value("${agora.app.certificate:YOUR_AGORA_APP_CERTIFICATE}")
    private String agoraAppCertificate;

    // In-memory storage for active video rooms (separate from your existing meeting system)
    private final Map<String, Map<String, Object>> activeVideoRooms = new ConcurrentHashMap<>();
    private final Map<String, Map<String, Object>> videoRoomParticipants = new ConcurrentHashMap<>();

    @Override
    public String generateToken(String channelName, String uid, String role) {
        try {
            // For now, return null for testing (Agora allows this in development)
            // In production, you should implement proper token generation
            logger.info("Generating Agora token for channel: {}, uid: {}, role: {}", channelName, uid, role);

            // TODO: Implement proper token generation using Agora's token builder
            // For development, you can use null token
            return null;

        } catch (Exception e) {
            logger.error("Error generating Agora token", e);
            throw new RuntimeException("Failed to generate token", e);
        }
    }

    @Override
    public Map<String, Object> createVideoRoom(String meetingId, String channelName, Users creator) {
        try {
            logger.info("Creating Agora video room for existing meeting: {}, channel: {}", meetingId, channelName);

            // This creates a video room for an existing meeting in your system
            Map<String, Object> videoRoomInfo = new HashMap<>();
            videoRoomInfo.put("videoRoomId", UUID.randomUUID().toString());
            videoRoomInfo.put("channelName", channelName);
            videoRoomInfo.put("meetingId", meetingId); // Links to your existing meeting
            videoRoomInfo.put("creatorId", creator.getId()); // Uses your existing user ID
            videoRoomInfo.put("creatorName", creator.getFullName()); // Uses your existing user data
            videoRoomInfo.put("createdAt", System.currentTimeMillis());
            videoRoomInfo.put("participantCount", 0);
            videoRoomInfo.put("status", "active");

            // Store video room information (separate from your meeting data)
            activeVideoRooms.put(channelName, videoRoomInfo);
            videoRoomParticipants.put(channelName, new HashMap<>());

            logger.info("Agora video room created successfully for meeting: {}", meetingId);
            return videoRoomInfo;

        } catch (Exception e) {
            logger.error("Error creating Agora video room", e);
            throw new RuntimeException("Failed to create video room", e);
        }
    }

    @Override
    public Map<String, Object> joinVideoRoom(String channelName, Users user) {
        try {
            logger.info("User {} joining Agora video room: {}", user.getUserName(), channelName);

            Map<String, Object> videoRoomInfo = activeVideoRooms.get(channelName);
            if (videoRoomInfo == null) {
                throw new RuntimeException("Video room not found: " + channelName);
            }

            // Add user to video room participants (separate from meeting participants)
            Map<String, Object> participants = videoRoomParticipants.get(channelName);
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("userId", user.getId()); // Your existing user ID
            userInfo.put("userName", user.getUserName()); // Your existing username
            userInfo.put("fullName", user.getFullName()); // Your existing full name
            userInfo.put("joinedAt", System.currentTimeMillis());
            userInfo.put("role", "publisher");

            participants.put(user.getId().toString(), userInfo);

            // Update participant count
            int participantCount = participants.size();
            videoRoomInfo.put("participantCount", participantCount);

            // Generate Agora token for user
            String token = generateToken(channelName, user.getId().toString(), "publisher");

            Map<String, Object> joinInfo = new HashMap<>();
            joinInfo.put("videoRoomInfo", videoRoomInfo);
            joinInfo.put("userInfo", userInfo);
            joinInfo.put("token", token);
            joinInfo.put("appId", agoraAppId);

            logger.info("User {} joined Agora video room: {} (total participants: {})",
                    user.getUserName(), channelName, participantCount);

            return joinInfo;

        } catch (Exception e) {
            logger.error("Error joining Agora video room", e);
            throw new RuntimeException("Failed to join video room", e);
        }
    }

    @Override
    public void leaveVideoRoom(String channelName, Users user) {
        try {
            logger.info("User {} leaving Agora video room: {}", user.getUserName(), channelName);

            Map<String, Object> participants = videoRoomParticipants.get(channelName);
            if (participants != null) {
                participants.remove(user.getId().toString());

                // Update participant count
                Map<String, Object> videoRoomInfo = activeVideoRooms.get(channelName);
                if (videoRoomInfo != null) {
                    int participantCount = participants.size();
                    videoRoomInfo.put("participantCount", participantCount);

                    // If no participants left, mark room as inactive
                    if (participantCount == 0) {
                        videoRoomInfo.put("status", "inactive");
                        logger.info("Agora video room {} is now inactive (no participants)", channelName);
                    }
                }
            }

            logger.info("User {} left Agora video room: {}", user.getUserName(), channelName);

        } catch (Exception e) {
            logger.error("Error leaving Agora video room", e);
            throw new RuntimeException("Failed to leave video room", e);
        }
    }

    @Override
    public Map<String, Object> getRoomParticipants(String channelName) {
        try {
            Map<String, Object> participants = videoRoomParticipants.get(channelName);
            if (participants == null) {
                return new HashMap<>();
            }

            Map<String, Object> result = new HashMap<>();
            result.put("channelName", channelName);
            result.put("participants", participants);
            result.put("participantCount", participants.size());

            return result;

        } catch (Exception e) {
            logger.error("Error getting Agora room participants", e);
            throw new RuntimeException("Failed to get room participants", e);
        }
    }

    @Override
    public boolean validateToken(String token, String channelName, String uid) {
        try {
            // For development with null tokens, always return true
            // In production, implement proper token validation
            if (token == null) {
                return true; // Allow null tokens in development
            }

            // TODO: Implement proper token validation
            logger.info("Validating Agora token for channel: {}, uid: {}", channelName, uid);
            return true;

        } catch (Exception e) {
            logger.error("Error validating Agora token", e);
            return false;
        }
    }

    // Helper methods
    public Map<String, Object> getAllActiveVideoRooms() {
        return new HashMap<>(activeVideoRooms);
    }

    public void cleanupInactiveVideoRooms() {
        long currentTime = System.currentTimeMillis();
        long timeout = 30 * 60 * 1000; // 30 minutes

        activeVideoRooms.entrySet().removeIf(entry -> {
            Map<String, Object> videoRoomInfo = entry.getValue();
            String status = (String) videoRoomInfo.get("status");
            Long createdAt = (Long) videoRoomInfo.get("createdAt");

            if ("inactive".equals(status) && createdAt != null) {
                return (currentTime - createdAt) > timeout;
            }
            return false;
        });

        logger.info("Cleaned up inactive Agora video rooms");
    }
}