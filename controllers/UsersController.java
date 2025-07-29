package com.Unite.UniteMobileApp.controllers;

import com.Unite.UniteMobileApp.dtos.UserProfileDTO;
import com.Unite.UniteMobileApp.entities.Users;
import com.Unite.UniteMobileApp.miscellaneous.SimpleMessage;
import com.Unite.UniteMobileApp.dtos.UsersDTO;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import com.Unite.UniteMobileApp.services.UsersServiceInterface;

import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UsersController {

    private final UsersServiceInterface usersService;
    private static final Logger logger = LoggerFactory.getLogger(UsersController.class);


    public UsersController(UsersServiceInterface usersService) {
        this.usersService = usersService;
        logger.info("UsersController instantiated");
    }

    // Register new user
    @PostMapping("/register")
    public ResponseEntity<UsersDTO.Response> registerUser(@Valid @RequestBody UsersDTO.Request requestDto) {
        logger.info("Registering user with email: {}", requestDto.getEmail());

        UsersDTO.Response createdUser = usersService.registerUser(requestDto);

        logger.info("User registered successfully with ID: {}", createdUser.getId());

        // Return the DTO with the token
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    // Login user
    @PostMapping("/login")
    public ResponseEntity<SimpleMessage> loginUser(@RequestBody UsersDTO.Login loginDto) {
        logger.info("Login attempt for email: {}", loginDto.getUserName());

        UsersDTO.Response user = usersService.loginUser(loginDto);

        logger.info("Login successful for user ID: {}", user.getId());

        return ResponseEntity.ok(new SimpleMessage("Login Successful!"));
    }

    // Get user by ID
    @GetMapping("/{id}")
    public ResponseEntity<UsersDTO.Response> getUserById(@PathVariable UUID id) {
        logger.info("Fetching user with ID: {}", id);

        UsersDTO.Response user = usersService.getUserById(id);

        logger.info("User fetched successfully: {}", user.getUserName());

        return ResponseEntity.ok(user);
    }

    // Update user details
    @PutMapping("/{id}")
    public ResponseEntity<SimpleMessage> updateUser(@PathVariable UUID id, @RequestBody UsersDTO.Request requestDto){
        logger.info("Updating user with ID: {}", id);

        UsersDTO.Response updatedUser = usersService.updateUser(id, requestDto);

        logger.info("User updated successfully: {}", updatedUser.getUserName());

        return ResponseEntity.ok(new SimpleMessage("User updated."));
    }

    // Delete user
    @DeleteMapping("/{id}")
    public ResponseEntity<SimpleMessage> deleteUser(@PathVariable UUID id) {
        SimpleMessage response = usersService.deleteUser(id);

        if (response.getMessage().equals("User not found")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/contacts")
    public ResponseEntity<Set<Users>> getContacts(@RequestParam UUID userId) {
        Set<Users> contacts = usersService.getContacts(userId);
        return ResponseEntity.ok(contacts);
    }

    @GetMapping("/profile")
    public UserProfileDTO getProfile(@AuthenticationPrincipal UserDetails userDetails) {
        return usersService.getProfile(userDetails.getUsername());
    }

}
