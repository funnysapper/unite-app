package com.Unite.UniteMobileApp.controllers;

import com.Unite.UniteMobileApp.services.AgoraServiceInterface;
import com.Unite.UniteMobileApp.miscellaneous.CurrentUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/agora")
@CrossOrigin(origins = "*")
public class AgoraController {

    @Autowired
    private AgoraServiceInterface agoraService;

    @Autowired
    private CurrentUserService currentUserService;

    /**
     * Generate Agora token for joining a video call
     */
    @PostMapping("/token")
    public ResponseEntity<Map<String, Object>> generateToken(
            @RequestParam String channelName,
            @RequestParam String uid,
            @RequestParam(defaultValue = "publisher") String role,
            @AuthenticationPrincipal UserDetails userDetails) {

        try {
            String token = agoraService.generateToken(channelName, uid, role);

            Map<String, Object> response = Map.of(
                    "token", token,
                    "channelName", channelName,
                    "uid", uid,
                    "role", role
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Create a video call room
     */
    @PostMapping("/room/create")
    public ResponseEntity<Map<String, Object>> createVideoRoom(
            @RequestParam String meetingId,
            @RequestParam String channelName,
            @AuthenticationPrincipal UserDetails userDetails) {

        try {
            var currentUser = currentUserService.getCurrentUser();
            Map<String, Object> roomInfo = agoraService.createVideoRoom(meetingId, channelName, currentUser);

            return ResponseEntity.ok(roomInfo);

        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Join a video call room
     */
    @PostMapping("/room/join")
    public ResponseEntity<Map<String, Object>> joinVideoRoom(
            @RequestParam String channelName,
            @AuthenticationPrincipal UserDetails userDetails) {

        try {
            var currentUser = currentUserService.getCurrentUser();
            Map<String, Object> joinInfo = agoraService.joinVideoRoom(channelName, currentUser);

            return ResponseEntity.ok(joinInfo);

        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Leave a video call room
     */
    @PostMapping("/room/leave")
    public ResponseEntity<Void> leaveVideoRoom(
            @RequestParam String channelName,
            @AuthenticationPrincipal UserDetails userDetails) {

        try {
            var currentUser = currentUserService.getCurrentUser();
            agoraService.leaveVideoRoom(channelName, currentUser);

            return ResponseEntity.ok().build();

        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Get room participants
     */
    @GetMapping("/room/{channelName}/participants")
    public ResponseEntity<Map<String, Object>> getRoomParticipants(
            @PathVariable String channelName) {

        try {
            Map<String, Object> participants = agoraService.getRoomParticipants(channelName);
            return ResponseEntity.ok(participants);

        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Validate Agora token
     */
    @PostMapping("/token/validate")
    public ResponseEntity<Map<String, Object>> validateToken(
            @RequestParam String token,
            @RequestParam String channelName,
            @RequestParam String uid) {

        try {
            boolean isValid = agoraService.validateToken(token, channelName, uid);

            Map<String, Object> response = Map.of(
                    "valid", isValid,
                    "channelName", channelName,
                    "uid", uid
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Get all active video rooms (admin only)
     */
    @GetMapping("/rooms/active")
    public ResponseEntity<Map<String, Object>> getActiveRooms() {
        try {
            // This would typically check for admin role
            // For now, just return the rooms
            return ResponseEntity.ok(Map.of("message", "Active rooms endpoint"));

        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}