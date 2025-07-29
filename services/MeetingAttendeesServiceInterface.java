package com.Unite.UniteMobileApp.services;

import com.Unite.UniteMobileApp.dtos.MeetingAttendeesDTO;
import java.util.List;
import java.util.UUID;

public interface MeetingAttendeesServiceInterface {

        MeetingAttendeesDTO.Response addAttendee(MeetingAttendeesDTO.Request request);

        List<MeetingAttendeesDTO.Response> getAllAttendees();

        List<MeetingAttendeesDTO.Response> getAttendeesByMeetingId(UUID meetingId);

        void removeAttendee(UUID id);

        MeetingAttendeesDTO.Response updateAttendee(UUID id, MeetingAttendeesDTO.Request request);
    }


