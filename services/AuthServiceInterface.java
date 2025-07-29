package com.Unite.UniteMobileApp.services;

import com.Unite.UniteMobileApp.dtos.AuthDTO;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface AuthServiceInterface {

    ResponseEntity<AuthDTO.AuthResponse> login(AuthDTO.AuthRequest request);

    ResponseEntity<AuthDTO.AuthResponse> refreshToken(String authHeader);

    ResponseEntity<Map<String, Object>> validateToken(String authHeader);

    ResponseEntity<Map<String, String>> logout(String authHeader);


}

