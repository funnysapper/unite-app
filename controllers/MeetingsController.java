package com.Unite.UniteMobileApp.controllers;

import com.Unite.UniteMobileApp.dtos.MeetingCreateDTO;
import com.Unite.UniteMobileApp.dtos.MeetingResponseDTO;
import com.Unite.UniteMobileApp.dtos.MeetingUpdateDTO;
import com.Unite.UniteMobileApp.dtos.MeetingUpdateRequest;
import com.Unite.UniteMobileApp.entities.Meetings;
import com.Unite.UniteMobileApp.serviceimpl.MeetingsService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/meetings")
public class MeetingsController {
    private final MeetingsService meetingsService;

    @PostMapping
    public MeetingResponseDTO createMeeting(
            @RequestBody MeetingCreateDTO dto,
            @AuthenticationPrincipal UserDetails userDetails) {
        return meetingsService.createMeeting(dto, userDetails.getUsername());
    }

    @GetMapping("/user/{userId}")
    public List<MeetingResponseDTO> getMeetingsForUser(@PathVariable UUID userId) {
        return meetingsService.getMeetingsForUser(userId);
    }

    @GetMapping("/{meetingId}")
    public MeetingResponseDTO getMeetingById(@PathVariable UUID meetingId) {
        return meetingsService.getMeetingById(meetingId);
    }

    @GetMapping("/range")
    public List<MeetingResponseDTO> getMeetingsForUserInRange(
            @RequestParam UUID userId,
            @RequestParam LocalDateTime start,
            @RequestParam LocalDateTime end
    ) {
        return meetingsService.getMeetingsForUserInRange(userId, start, end);
    }

    @PostMapping("/join/{code}")
    public ResponseEntity<MeetingResponseDTO> joinMeetingByCode(
            @PathVariable String code,
            @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        MeetingResponseDTO dto = meetingsService.joinMeetingByCode(code, username);
        return ResponseEntity.ok(dto);
    }

    @PatchMapping("/{meetingId}")
    public ResponseEntity<MeetingResponseDTO> updateMeeting(
            @PathVariable UUID meetingId,
            @RequestBody MeetingUpdateRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        try {
            MeetingResponseDTO updatedMeeting = meetingsService.updateMeeting(meetingId, request);
            return ResponseEntity.ok(updatedMeeting);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{meetingId}")
    public ResponseEntity<Void> deleteMeeting(
            @PathVariable UUID meetingId,
            @AuthenticationPrincipal UserDetails userDetails) {

        try {
            meetingsService.deleteMeeting(meetingId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}