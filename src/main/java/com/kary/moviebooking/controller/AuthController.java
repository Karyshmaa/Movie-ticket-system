package com.kary.moviebooking.controller;

import com.kary.moviebooking.dto.ForgotPasswordRequestDTO;
import com.kary.moviebooking.dto.ResetPasswordRequestDTO;
import com.kary.moviebooking.dto.SignupRequestDTO;
import com.kary.moviebooking.enums.Role;
import com.kary.moviebooking.entity.User;
import com.kary.moviebooking.repository.UserRepository;
import com.kary.moviebooking.security.JwtUtil;
import com.kary.moviebooking.service.Interface.Authservice;
import com.kary.moviebooking.service.Interface.PasswordResetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final Authservice authservice;
    private final PasswordResetService passwordResetService;

    @PostMapping("/signup")
    public String signup(@RequestBody SignupRequestDTO request) {
      return authservice.signup(request);
    }

    @GetMapping("/verify")
    public String verify(@RequestParam String token) {
        return authservice.verify(token);
    }

    @PostMapping("/login")
    public String login(@RequestBody User user) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getEmail(),
                        user.getPassword()
                )
        );

        Role role = userRepository
                .findByEmail(user.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"))
                .getRole();

        return jwtUtil.generateToken(user.getEmail(), role.name());
    }
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(
            @RequestBody @Valid ForgotPasswordRequestDTO request) {

        passwordResetService.forgotPassword(request);

        // ✅ always return same message whether email exists or not
        return ResponseEntity.ok("If this email is registered, a reset link has been sent");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(
            @RequestBody @Valid ResetPasswordRequestDTO request) {

        passwordResetService.resetPassword(request);
        return ResponseEntity.ok("Password reset successful. You can now log in");
    }
}

