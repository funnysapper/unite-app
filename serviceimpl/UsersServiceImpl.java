package com.Unite.UniteMobileApp.serviceimpl;

import com.Unite.UniteMobileApp.dtos.UserProfileDTO;
import com.Unite.UniteMobileApp.entities.TeamMembers;
import com.Unite.UniteMobileApp.miscellaneous.SimpleMessage;
import com.Unite.UniteMobileApp.exceptions.EmailAlreadyExistsException;
import com.Unite.UniteMobileApp.exceptions.EmailRequiredException;
import com.Unite.UniteMobileApp.exceptions.InvalidEmailOrPasswordException;
import com.Unite.UniteMobileApp.exceptions.InvalidPhoneNumberException;
import com.Unite.UniteMobileApp.repositories.TeamMembersRepository;
import com.Unite.UniteMobileApp.utils.JwtUtils;
import com.Unite.UniteMobileApp.utils.PhoneNumberValidator;
import com.Unite.UniteMobileApp.dtos.UsersDTO;
import com.Unite.UniteMobileApp.entities.Users;
import com.Unite.UniteMobileApp.mappers.UsersMapper;
import com.Unite.UniteMobileApp.repositories.UsersRepository;
import com.Unite.UniteMobileApp.services.UsersServiceInterface;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class UsersServiceImpl implements UsersServiceInterface {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private final TeamMembersRepository teamMembersRepository;
    private final UserDetailsService userDetailsService;
    private final JwtUtils jwtUtils;


    @Override
     public UsersDTO.Response registerUser(UsersDTO.Request requestDto) {

        if (requestDto.getEmail() == null || requestDto.getEmail().isEmpty()) {
            throw new EmailRequiredException("Users email is required");
        }

        if (usersRepository.findByEmail(requestDto.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("Email already exists: " + requestDto.getEmail());
        }

        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        Phonenumber.PhoneNumber parsedNumber;

        try {
            String regionCode = requestDto.getRegionCode().toUpperCase(); // ✅ convert to uppercase
            String phoneNumber = requestDto.getPhoneNumber();

            if (phoneNumber.startsWith("+")) {
                parsedNumber = phoneUtil.parse(phoneNumber, null); // international format
            } else {
                parsedNumber = phoneUtil.parse(phoneNumber, regionCode); // local + region code
            }

            if (!phoneUtil.isValidNumber(parsedNumber)) {
                throw new InvalidPhoneNumberException("Invalid phone number for region: " +
                        regionCode + " " + phoneNumber);
            }

            // ✅ Format to international +233xxxxxxx style
            phoneNumber = phoneUtil.format(parsedNumber, PhoneNumberUtil.PhoneNumberFormat.E164);

            String hashedPassword = passwordEncoder.encode(requestDto.getPassword());

            Users user = Users.builder()
                    .fullName(requestDto.getFullName())
                    .email(requestDto.getEmail())
                    .phoneNumber(phoneNumber)
                    .password(hashedPassword)
                    .userName(requestDto.getUserName())
                    .regionCode(regionCode)
                    .verified(true)
                    .build();

            Users savedUser = usersRepository.save(user);
            UserDetails userDetails =   userDetailsService.loadUserByUsername(savedUser.getUserName());
            String token = jwtUtils.generateToken(userDetails);
            UsersDTO.Response dto = UsersMapper.toDto(savedUser);
            dto.setToken(token);
            return dto;

        } catch (NumberParseException e) {
            throw new InvalidPhoneNumberException("Error parsing phone number: " + e.getMessage());
        }
    }


    @Override
    public UsersDTO.Response loginUser(UsersDTO.Login loginDto) {

        Optional<Users> optionalUser = usersRepository.findByUserName(loginDto.getUserName());

        if (optionalUser.isPresent()) {
            Users user = optionalUser.get();

            if (passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
                return UsersMapper.toDto(user);
            }
        }
        throw new InvalidEmailOrPasswordException("Invalid username or password");

    }


    @Override
    public UsersDTO.Response getUserById(UUID id) {
        Users user = usersRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return UsersMapper.toDto(user);
    }


    @Transactional
    @Override
    public UsersDTO.Response updateUser(UUID id, UsersDTO.Request requestDto) {
        Users user = usersRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (requestDto.getUserName() != null && !requestDto.getUserName().isEmpty()) {
            user.setUserName(requestDto.getUserName());
        }
        if (requestDto.getFullName() != null && !requestDto.getFullName().isEmpty()) {
            user.setFullName(requestDto.getFullName());
        }
        if (requestDto.getEmail() != null && !requestDto.getEmail().isEmpty()) {
            user.setEmail(requestDto.getEmail());
        }
        if (requestDto.getPhoneNumber() != null && !requestDto.getPhoneNumber().isEmpty()) {
            user.setPhoneNumber(requestDto.getPhoneNumber());
        }
        if (requestDto.getPassword() != null && !requestDto.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        }
        Users updatedUser = usersRepository.save(user);

        String newUsername = requestDto.getUserName();

        List<TeamMembers> asMember = teamMembersRepository.findByUserId(id);
        for (TeamMembers member : asMember) {
            member.setUserName(newUsername);
        }
        teamMembersRepository.saveAll(asMember);

        // ✅ Update all team member records where this user added others
        List<TeamMembers> asAdder = teamMembersRepository.findByAddedByUserId(id);
        for (TeamMembers member : asAdder) {
            member.setAddedByUserName(newUsername);
        }
        teamMembersRepository.saveAll(asAdder);

        return UsersMapper.toDto(updatedUser);
    }

    @Override
    public SimpleMessage deleteUser(UUID id) {
        Optional<Users> userOptional = usersRepository.findById(id);

        if (userOptional.isEmpty()) {
            return new SimpleMessage("User not found");
        }

        usersRepository.deleteById(id);
        return new SimpleMessage("User deleted successfully");
    }

    public Set<Users> getContacts(UUID userId) {
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user.getContacts();
    }

    public UserProfileDTO getProfile(String username) {
        Users user = usersRepository.findByUserName(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return new UserProfileDTO( user.getId(),user.getFullName(), user.getEmail(), user.getUserName());
    }
}


