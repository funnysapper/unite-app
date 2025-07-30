package com.Unite.UniteMobileApp.controllers;


import com.Unite.UniteMobileApp.entities.Meeting;
import com.Unite.UniteMobileApp.serviceimpl.MeetingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/meeting")
@RequiredArgsConstructor
public class MeetingController {

    private final MeetingService meetingService;

    @PostMapping("/create")
    public ResponseEntity<Map<String, String>> createCall(@RequestParam String createdBy) {
        Meeting meeting = meetingService.createMeeting(createdBy);
        return ResponseEntity.ok(Map.of("callID", meeting.getCallID()));
    }

    @GetMapping("/{callID}")
    public ResponseEntity<Meeting> getMeeting(@PathVariable String callID) {
        return ResponseEntity.ok(meetingService.getMeetingByCallID(callID));
    }
}
