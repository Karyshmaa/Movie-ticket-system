package com.kary.moviebooking.service.Impl;

import com.kary.moviebooking.dto.UserRequestDTO;
import com.kary.moviebooking.dto.UserResponseDTO;
import com.kary.moviebooking.enums.Role;
import com.kary.moviebooking.exception.BadRequestException;
import com.kary.moviebooking.exception.ResourceNotFoundException;
import com.kary.moviebooking.repository.UserRepository;
import com.kary.moviebooking.service.Interface.UserService;
import lombok.RequiredArgsConstructor;
import com.kary.moviebooking.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;  // ✅ encode password before saving

    @Override
    public UserResponseDTO createUser(UserRequestDTO request) {

        // check duplicate email
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new BadRequestException("Email already registered: " + request.getEmail());
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword())); // ✅ never plain text
        user.setRole(Role.USER);
        user.setActive(true);

        // @PrePersist handles createdAt — don't set it manually
        userRepository.save(user);

        return toDTO(user);
    }

    @Override
    public UserResponseDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + id));
        return toDTO(user);
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    //private mapper — keeps DTO conversion in one place
    private UserResponseDTO toDTO(User user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole() != null ? user.getRole().name() : "USER")
                .active(user.isActive())
                .createdAt(user.getCreatedAt())
                .build();
    }
}

