package com.Unite.UniteMobileApp.miscellaneous;

import com.Unite.UniteMobileApp.entities.Users;
import com.Unite.UniteMobileApp.repositories.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CurrentUserService {

    private final UsersRepository usersRepository;

    public Users getCurrentUser() {
        // The authentication system uses username, not email
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return usersRepository.findByUserName(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}