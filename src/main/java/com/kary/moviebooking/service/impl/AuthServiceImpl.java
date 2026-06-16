package com.kary.moviebooking.service.Impl;

import com.kary.moviebooking.dto.LoginRequestDTO;
import com.kary.moviebooking.dto.LoginResponseDTO;
import com.kary.moviebooking.dto.SignupRequestDTO;
import com.kary.moviebooking.entity.User;
import com.kary.moviebooking.entity.VerificationToken;
import com.kary.moviebooking.enums.Role;
import com.kary.moviebooking.exception.BadRequestException;
import com.kary.moviebooking.exception.ResourceNotFoundException;
import com.kary.moviebooking.repository.UserRepository;
import com.kary.moviebooking.repository.VerificationTokenRepository;
import com.kary.moviebooking.security.JwtUtil;
import com.kary.moviebooking.service.Interface.Authservice;
import com.kary.moviebooking.service.Interface.EmailService;
import com.kary.moviebooking.service.Interface.VerificationTokenService;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements Authservice {

    private final UserRepository userRepository;
    private final VerificationTokenService tokenService;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenRepository verificationTokenRepository;
    private final AuthenticationManager authenticationManager;  // ✅ added
    private final JwtUtil jwtUtil;

    public AuthServiceImpl(UserRepository userRepository,
                           VerificationTokenService tokenService,
                           EmailService emailService,
                           PasswordEncoder passwordEncoder,
                           VerificationTokenRepository verificationTokenRepository,
                           @Lazy AuthenticationManager authenticationManager,
                           JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.tokenService = tokenService;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
        this.verificationTokenRepository = verificationTokenRepository;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }


    @Override
    public String signup(SignupRequestDTO request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new BadRequestException("Email already registered: " + request.getEmail());
        }

        User user = new User();
        user.setName(request.getName()); // make sure DTO has this
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);
        user.setActive(true); // use this instead of enabled
        userRepository.save(user);
        return "Signup successful";
    }

        public String verify(String token) {
            VerificationToken vt = verificationTokenRepository.findByToken(token)
                    .orElseThrow(() -> new RuntimeException("Invalid token"));

            User user = vt.getUser();
            user.setActive(true);
            userRepository.save(user);
            return "Email verified successfully";
    }

    @Override
    public LoginResponseDTO login(LoginRequestDTO request) {

        // 1. authenticate
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // 2. fetch user
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // 3. generate token
        String token = jwtUtil.generateToken(
                user.getEmail(),
                user.getRole().name()
        );

        return new LoginResponseDTO(token, user.getRole().name(), user.getEmail());
    }
}
