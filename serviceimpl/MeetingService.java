package com.Unite.UniteMobileApp.serviceimpl;



import com.Unite.UniteMobileApp.CallIDGenerator;
import com.Unite.UniteMobileApp.entities.Meeting;
import com.Unite.UniteMobileApp.repositories.MeetingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class MeetingService {

    private final MeetingRepository meetingRepository;

    public Meeting createMeeting(String createdBy) {
        String callID = CallIDGenerator.generateCallID();

        Meeting meeting = Meeting.builder()
                .callID(callID)
                .createdBy(createdBy)
                .createdAt(OffsetDateTime.now())
                .build();

        return meetingRepository.save(meeting);
    }

    public Meeting getMeetingByCallID(String callID) {
        return meetingRepository.findByCallID(callID)
                .orElseThrow(() -> new RuntimeException("Meeting not found"));
    }
}
