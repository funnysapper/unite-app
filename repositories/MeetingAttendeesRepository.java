package com.Unite.UniteMobileApp.repositories;

import com.Unite.UniteMobileApp.entities.MeetingAttendees;
import com.Unite.UniteMobileApp.entities.Meetings;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface MeetingAttendeesRepository extends JpaRepository<MeetingAttendees, UUID> {
    List<MeetingAttendees> findByMeeting(Meetings meeting);
}


