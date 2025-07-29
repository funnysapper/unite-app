package com.Unite.UniteMobileApp.services;

import com.Unite.UniteMobileApp.dtos.ActivityDTO;
import com.Unite.UniteMobileApp.entities.Users;
import java.util.List;

public interface ActivityServiceInterface {
    List<ActivityDTO> getActivityForUser(Users user);
    void logActivity(Users user, String type, String message);
}