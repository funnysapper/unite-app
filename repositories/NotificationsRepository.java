package com.Unite.UniteMobileApp.repositories;

import com.Unite.UniteMobileApp.entities.Notifications;
import com.Unite.UniteMobileApp.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface NotificationsRepository extends JpaRepository<Notifications, Long> {
    List<Notifications> findByUserId(UUID userId);

    boolean existsById(UUID notificationId);

    Optional<Notifications> findById(UUID notificationId);

    void deleteById(UUID notificationId);
}



