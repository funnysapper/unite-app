package com.Unite.UniteMobileApp.mappers;
import com.Unite.UniteMobileApp.dtos.UsersDTO;
import com.Unite.UniteMobileApp.entities.Users;
import org.springframework.stereotype.Component;

@Component
public class UsersMapper {

        // Convert UserRequestDto to Users entity (used when receiving data from frontend)
        public static Users toEntity(UsersDTO.Request dto) {
            return Users.builder()
                    .fullName(dto.getFullName())
                    .email(dto.getEmail())
                    .phoneNumber(dto.getPhoneNumber())
                    .regionCode(dto.getRegionCode())
                    .userName(dto.getUserName())
                    .password(dto.getPassword())
                    .build();
        }

        // Convert Users entity to UserResponseDto (used when sending data to frontend)
        public static UsersDTO.Response toDto(Users user) {
            return UsersDTO.Response.builder()
                    .id(user.getId())
                    .fullName(user.getFullName())
                    .email(user.getEmail())
                    .phoneNumber(user.getPhoneNumber())
                    .regionCode(user.getRegionCode())
                    .verified(user.isVerified())
                    .build();
        }
    }








