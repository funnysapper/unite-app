package com.Unite.UniteMobileApp.serviceimpl;

import com.Unite.UniteMobileApp.dtos.MeetingAttendeesDTO;
import com.Unite.UniteMobileApp.entities.MeetingAttendees;
import com.Unite.UniteMobileApp.entities.Meetings;
import com.Unite.UniteMobileApp.mappers.MeetingAttendeesMapper;
import com.Unite.UniteMobileApp.repositories.MeetingAttendeesRepository;
import com.Unite.UniteMobileApp.repositories.MeetingsRepository;
import com.Unite.UniteMobileApp.services.MeetingAttendeesServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

    @Service
    @RequiredArgsConstructor
    public class MeetingAttendeesServiceImpl implements MeetingAttendeesServiceInterface {

        private final MeetingAttendeesRepository repository;
        private final MeetingAttendeesMapper mapper;
        private final MeetingsRepository meetingsRepository;

        @Override
        public MeetingAttendeesDTO.Response addAttendee(MeetingAttendeesDTO.Request request) {
            MeetingAttendees attendee = MeetingAttendeesMapper.toEntity(request);
            MeetingAttendees saved = repository.save(attendee);
            return MeetingAttendeesMapper.toDto(saved);
        }

        @Override
        public List<MeetingAttendeesDTO.Response> getAllAttendees() {
            return repository.findAll().stream()
                    .map(MeetingAttendeesMapper::toDto)
                    .collect(Collectors.toList());
        }

        @Override
        public List<MeetingAttendeesDTO.Response> getAttendeesByMeetingId(UUID meetingId) {
            Meetings meeting = meetingsRepository.findById(meetingId)
                    .orElseThrow(() -> new RuntimeException("Meeting not found with id: " + meetingId));
            return repository.findByMeeting(meeting).stream()
                    .map(MeetingAttendeesMapper::toDto)
                    .collect(Collectors.toList());
        }

        @Override
        public void removeAttendee(UUID id) {
            if (!repository.existsById(id)) {
                throw new RuntimeException("Attendee not found with id: " + id);
            }
            repository.deleteById(id);
        }

        @Override
        public MeetingAttendeesDTO.Response updateAttendee(UUID id, MeetingAttendeesDTO.Request request) {
            MeetingAttendees existing = repository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Attendee not found with id: " + id));

            existing.setMeeting(request.getMeeting());
            existing.setUser(request.getUser());
            existing.setJoinedAt(request.getJoinedAt());
            existing.setLeftAt(request.getLeftAt());
            existing.setRole(request.getRole());

            MeetingAttendees updated = repository.save(existing);
            return MeetingAttendeesMapper.toDto(updated);
        }
    }


