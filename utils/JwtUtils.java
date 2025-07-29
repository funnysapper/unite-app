package com.Unite.UniteMobileApp.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtUtils {

    // Use application.properties for configuration
    @Value("${app.jwt.secret:mySecretKey02465562780509119367024404289402447855260240233324}")
    private String jwtSecret;

    @Value("${app.jwt.expiration:86400000}")  // 1 day in milliseconds
    private int jwtExpirationMs;

    @Value("${app.jwt.refresh-expiration:604800000}")  // 7 days in milliseconds
    private int refreshExpirationMs;

    // Generate secret key from string
    private SecretKey getSigningKey() {
        byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Generate token with default expiration
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        List<String> roles = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        claims.put("roles", roles);
        return createToken(claims, userDetails.getUsername(), jwtExpirationMs);
    }

    // Generate refresh token with longer expiration
    public String generateRefreshToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("type", "refresh");
        return createToken(claims, userDetails.getUsername(), refreshExpirationMs);
    }


    // Create token with claims and expiration
    private String createToken(Map<String, Object> claims, String subject, long expiration) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Extract username from token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extract expiration date from token
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Extract issued at date from token
    public Date extractIssuedAt(String token) {
        return extractClaim(token, Claims::getIssuedAt);
    }

    // Extract specific claim from token
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Extract all claims from token
    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException e) {
            log.error("Error extracting claims from token: {}", e.getMessage());
            throw e;
        }
    }

    // Check if token is expired
    public boolean isTokenExpired(String token) {
        try {
            Date expiration = extractExpiration(token);
            return expiration.before(new Date());
        } catch (JwtException e) {
            log.warn("Token expiration check failed: {}", e.getMessage());
            return true; // Consider invalid tokens as expired
        }
    }

    // Validate token against user details
    public boolean validateToken(String token, UserDetails userDetails) {
        try {
            final String username = extractUsername(token);
            return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
        } catch (JwtException e) {
            log.warn("Token validation failed: {}", e.getMessage());
            return false;
        }
    }

    // Simple token validation (just checks if token is valid and not expired)
    public boolean validateToken(String token) {
        try {
            Claims claims = extractAllClaims(token);
            return !isTokenExpired(token);
        } catch (JwtException e) {
            log.warn("Token validation failed: {}", e.getMessage());
            return false;
        }
    }

    // Check if token can be refreshed (not expired beyond refresh window)
    public boolean canTokenBeRefreshed(String token) {
        try {
            Date expiration = extractExpiration(token);
            Date now = new Date();
            // Allow refresh if token expired less than 1 hour ago
            long oneHour = 3600000; // 1 hour in milliseconds
            return expiration.after(new Date(now.getTime() - oneHour));
        } catch (JwtException e) {
            log.warn("Token refresh check failed: {}", e.getMessage());
            return false;
        }
    }

    // Get remaining time until token expires (in milliseconds)
    public long getTokenExpirationTime(String token) {
        try {
            Date expiration = extractExpiration(token);
            return expiration.getTime() - new Date().getTime();
        } catch (JwtException e) {
            log.warn("Error getting token expiration time: {}", e.getMessage());
            return 0;
        }
    }

    // Check if token is a refresh token
    public boolean isRefreshToken(String token) {
        try {
            Claims claims = extractAllClaims(token);
            return "refresh".equals(claims.get("type"));
        } catch (JwtException e) {
            log.warn("Error checking token type: {}", e.getMessage());
            return false;
        }
    }

    // Get token type (access or refresh)
    public String getTokenType(String token) {
        try {
            Claims claims = extractAllClaims(token);
            return (String) claims.getOrDefault("type", "access");
        } catch (JwtException e) {
            log.warn("Error getting token type: {}", e.getMessage());
            return "unknown";
        }
    }

    // Legacy method for backward compatibility
    public String getUsernameFromToken(String token) {
        return extractUsername(token);
    }

    // Get all custom claims from token
    public Map<String, Object> getCustomClaims(String token) {
        try {
            Claims claims = extractAllClaims(token);
            Map<String, Object> customClaims = new HashMap<>(claims);
            // Remove standard claims
            customClaims.remove("sub");
            customClaims.remove("iat");
            customClaims.remove("exp");
            customClaims.remove("iss");
            customClaims.remove("aud");
            return customClaims;
        } catch (JwtException e) {
            log.warn("Error getting custom claims: {}", e.getMessage());
            return new HashMap<>();
        }
    }
}