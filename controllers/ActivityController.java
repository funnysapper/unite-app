package com.Unite.UniteMobileApp.controllers;

import com.Unite.UniteMobileApp.dtos.ActivityDTO;
import com.Unite.UniteMobileApp.entities.Users;
import com.Unite.UniteMobileApp.repositories.UsersRepository;
import com.Unite.UniteMobileApp.services.ActivityServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ActivityController {

    private final UsersRepository usersRepository;
    private final ActivityServiceInterface activityService;


    @GetMapping("/activity")
    public ResponseEntity<List<ActivityDTO>> getUserActivity(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        // Find the user
        Users user = usersRepository.findByUserName(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Fetch or generate activity feed for this user
        List<ActivityDTO> activities = activityService.getActivityForUser(user);

        return ResponseEntity.ok(activities);
    }
}