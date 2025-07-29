package com.Unite.UniteMobileApp.services;

import com.Unite.UniteMobileApp.dtos.UserProfileDTO;
import com.Unite.UniteMobileApp.entities.Users;
import com.Unite.UniteMobileApp.miscellaneous.SimpleMessage;
import com.Unite.UniteMobileApp.dtos.UsersDTO;

import java.util.Set;
import java.util.UUID;

public interface UsersServiceInterface {

    // Register a new user
    UsersDTO.Response registerUser(UsersDTO.Request requestDto);

    // Login a user
    UsersDTO.Response loginUser(UsersDTO.Login loginDto);

    // Get user by ID (optional but common)
    UsersDTO.Response getUserById(UUID id);

    // Update user details (optional)
    UsersDTO.Response updateUser(UUID id, UsersDTO.Request requestDto);

    // Delete user (optional)
    SimpleMessage deleteUser(UUID id);

    Set<Users> getContacts(UUID userId);

    UserProfileDTO getProfile(String username);
}
