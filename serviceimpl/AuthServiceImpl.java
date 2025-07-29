package com.Unite.UniteMobileApp.serviceimpl;

import com.Unite.UniteMobileApp.dtos.AuthDTO;
import com.Unite.UniteMobileApp.dtos.UsersDTO;
import com.Unite.UniteMobileApp.entities.Users;
import com.Unite.UniteMobileApp.mappers.UsersMapper;
import com.Unite.UniteMobileApp.repositories.UsersRepository;
import com.Unite.UniteMobileApp.services.AuthServiceInterface;
import com.Unite.UniteMobileApp.services.UsersServiceInterface;
import com.Unite.UniteMobileApp.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthServiceInterface {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserDetailsService userDetailsService;
    private final UsersRepository usersRepository;



    @Override
    public ResponseEntity<AuthDTO.AuthResponse> login(AuthDTO.AuthRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
            String token = jwtUtils.generateToken(userDetails);

            // Fetch user info
            Users user = usersRepository.findByUserName(request.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            UsersDTO.Response userDto = UsersMapper.toDto(user);

            AuthDTO.AuthResponse response = new AuthDTO.AuthResponse(
                    token,
                    null,
                    "Login successful",
                    userDto
            );

            return ResponseEntity.ok(response);

        } catch (BadCredentialsException | UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthDTO.AuthResponse(null, null, "Invalid credentials", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AuthDTO.AuthResponse(null, null, "Login failed", null));
        }
    }

    @Override
    public ResponseEntity<AuthDTO.AuthResponse> refreshToken(String authHeader) {
        try {
            String token = extractTokenFromHeader(authHeader);

            if (token == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new AuthDTO.AuthResponse(null, null, "Invalid authorization header", null));
            }

            if (!jwtUtils.isRefreshToken(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new AuthDTO.AuthResponse(null, null, "Invalid token type - refresh token required", null));
            }

            if (!jwtUtils.validateToken(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new AuthDTO.AuthResponse(null, null, "Invalid or expired refresh token", null));
            }

            String username = jwtUtils.extractUsername(token);

            if (username == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new AuthDTO.AuthResponse(null, null, "Unable to extract username from token", null));
            }

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            String newAccessToken = jwtUtils.generateToken(userDetails);
            String newRefreshToken = jwtUtils.generateRefreshToken(userDetails);

            log.info("Token refreshed successfully for username: {}", username);

            return ResponseEntity.ok()
                    .header("Authorization", "Bearer " + newAccessToken)
                    .body(new AuthDTO.AuthResponse(newAccessToken, newRefreshToken, "Token refreshed successfully", null));

        } catch (UsernameNotFoundException e) {
            log.warn("User not found during token refresh: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthDTO.AuthResponse(null, null, "User not found", null));
        } catch (Exception e) {
            log.error("Token refresh failed", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AuthDTO.AuthResponse(null, null, "Token refresh failed", null));
        }
    }

    @Override
    public ResponseEntity<Map<String, Object>> validateToken(String authHeader) {
        Map<String, Object> response = new HashMap<>();
        try {
            String token = extractTokenFromHeader(authHeader);

            if (token == null || !jwtUtils.validateToken(token)) {
                response.put("valid", false);
                response.put("message", "Invalid or expired token");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            String username = jwtUtils.extractUsername(token);
            if (username == null) {
                response.put("valid", false);
                response.put("message", "Unable to extract username from token");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            response.put("valid", true);
            response.put("username", username);
            response.put("tokenType", jwtUtils.getTokenType(token));

            return ResponseEntity.ok(response);

        } catch (UsernameNotFoundException e) {
            log.warn("User not found during token validation: {}", e.getMessage());
            response.put("valid", false);
            response.put("message", "User not found");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        } catch (Exception e) {
            log.error("Token validation failed", e);
            response.put("valid", false);
            response.put("message", "Token validation failed");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @Override
    public ResponseEntity<Map<String, String>> logout(String authHeader) {
        Map<String, String> response = new HashMap<>();
        try {
            String token = extractTokenFromHeader(authHeader);

            if (token == null || !jwtUtils.validateToken(token)) {
                response.put("message", "Invalid or expired token");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            String username = jwtUtils.extractUsername(token);

            // TODO: Add token blacklist if needed
            response.put("message", "Logout successful");
            log.info("User {} logged out successfully", username);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Logout failed", e);
            response.put("message", "Logout failed");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    private String extractTokenFromHeader(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }
}

