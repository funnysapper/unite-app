package com.Unite.UniteMobileApp.repositories;

import com.Unite.UniteMobileApp.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UsersRepository extends JpaRepository<Users, UUID> {
        Optional<Users> findByEmail(String email);

        Optional<Users> findByUserName(String userName);

        Optional<Users> findByPhoneNumber(String phoneNumber);

        Optional<Users> findById(UUID id);

        void deleteById(UUID id);


}


