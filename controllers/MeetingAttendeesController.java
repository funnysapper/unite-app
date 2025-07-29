package com.Unite.UniteMobileApp.controllers;

import com.Unite.UniteMobileApp.dtos.MeetingAttendeesDTO;
import com.Unite.UniteMobileApp.services.MeetingAttendeesServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/meeting-attendees")
@RequiredArgsConstructor
public class MeetingAttendeesController {

    private final MeetingAttendeesServiceInterface attendeesService;

    // Add a new attendee
    @PostMapping
    public ResponseEntity<MeetingAttendeesDTO.Response> addAttendee(@RequestBody MeetingAttendeesDTO.Request request) {
        MeetingAttendeesDTO.Response response = attendeesService.addAttendee(request);
        return ResponseEntity.ok(response);
    }

    // Get all attendees
    @GetMapping
    public ResponseEntity<List<MeetingAttendeesDTO.Response>> getAllAttendees() {
        return ResponseEntity.ok(attendeesService.getAllAttendees());
    }

    // Get attendees by meeting ID
    @GetMapping("/meeting/{meetingId}")
    public ResponseEntity<List<MeetingAttendeesDTO.Response>> getAttendeesByMeetingId(@PathVariable UUID meetingId) {
        return ResponseEntity.ok(attendeesService.getAttendeesByMeetingId(meetingId));
    }

    // Update an attendee by ID
    @PutMapping("/{id}")
    public ResponseEntity<MeetingAttendeesDTO.Response> updateAttendee(
            @PathVariable UUID id,
            @RequestBody MeetingAttendeesDTO.Request request
    ) {
        return ResponseEntity.ok(attendeesService.updateAttendee(id, request));
    }

    // Delete an attendee by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeAttendee(@PathVariable UUID id) {
        attendeesService.removeAttendee(id);
        return ResponseEntity.noContent().build();
    }
}
