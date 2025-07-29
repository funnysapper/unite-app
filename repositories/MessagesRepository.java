package com.Unite.UniteMobileApp.repositories;

import com.Unite.UniteMobileApp.entities.Chat;
import com.Unite.UniteMobileApp.entities.Messages;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MessagesRepository extends JpaRepository<Messages, Long> {
    List<Messages> findByChat(Chat chat);

    boolean existsById(UUID messageId);
    void deleteById(UUID messageId);
}



