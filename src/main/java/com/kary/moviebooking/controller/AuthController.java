package com.kary.moviebooking.controller;

import com.kary.moviebooking.enums.Role;
import com.kary.moviebooking.entity.User;
import com.kary.moviebooking.enums.Role;
import com.kary.moviebooking.repository.UserRepository;
import com.kary.moviebooking.security.JwtUtil;
import com.kary.moviebooking.service.Interface.Authservice;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final Authservice authservice;

    public AuthController(UserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          AuthenticationManager authenticationManager,
                          JwtUtil jwtUtil, Authservice authservice) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.authservice = authservice;
    }

    @PostMapping("/signup")
    public String signup() {
        authservice.signup();
        return "Signup successful. Check email.";
    }

    @PostMapping("/login")
    public String login(@RequestBody User user) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        user.getPassword()
                )
        );

        Role role = userRepository
                .findByUsername(user.getUsername())
                .get()
                .getRole();

        return jwtUtil.generateToken(user.getUsername(), role.name());
    }
}
