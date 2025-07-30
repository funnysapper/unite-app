package com.Unite.UniteMobileApp.repositories;

import com.Unite.UniteMobileApp.entities.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MeetingRepository extends JpaRepository<Meeting, Long> {
    Optional<Meeting> findByCallID(String callID);
}
