package com.Unite.UniteMobileApp.serviceimpl;

import com.Unite.UniteMobileApp.dtos.ActivityDTO;
import com.Unite.UniteMobileApp.entities.Activity;
import com.Unite.UniteMobileApp.entities.Users;
import com.Unite.UniteMobileApp.repositories.ActivityRepository;
import com.Unite.UniteMobileApp.services.ActivityServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ActivityServiceImpl implements ActivityServiceInterface {
    private final ActivityRepository activityRepository;

    @Override
    public List<ActivityDTO> getActivityForUser(Users user) {
        List<Activity> activityEntities = activityRepository.findByUserOrderByTimeDesc(user);
        return activityEntities.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void logActivity(Users user, String type, String message) {
        Activity activity = Activity.builder()
                .user(user)
                .type(type)
                .message(message)
                .time(LocalDateTime.now())
                .build();

        activityRepository.save(activity);
    }

    private ActivityDTO mapToDTO(Activity activity) {
        return ActivityDTO.builder()
                .id(activity.getId())
                .type(activity.getType())
                .message(activity.getMessage())
                .time(activity.getTime())
                .build();
    }
}