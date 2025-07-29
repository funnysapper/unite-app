package com.Unite.UniteMobileApp.mappers;

import com.Unite.UniteMobileApp.dtos.MeetingAttendeesDTO;
import com.Unite.UniteMobileApp.entities.MeetingAttendees;
import org.springframework.stereotype.Component;

@Component
    public class MeetingAttendeesMapper {

        public static MeetingAttendeesDTO.Response toDto(MeetingAttendees attendee) {
            return MeetingAttendeesDTO.Response.builder()
                    .id(attendee.getId())
                    .meeting(attendee.getMeeting())
                    .user(attendee.getUser())
                    .joinedAt(attendee.getJoinedAt())
                    .leftAt(attendee.getLeftAt())
                    .role(attendee.getRole())
                    .build();
        }

        public static MeetingAttendees toEntity(MeetingAttendeesDTO.Request dto) {
            return MeetingAttendees.builder()
                    .id(dto.getId())
                    .meeting(dto.getMeeting())
                    .user(dto.getUser())
                    .joinedAt(dto.getJoinedAt())
                    .leftAt(dto.getLeftAt())
                    .role(dto.getRole())
                    .build();
        }
    }


