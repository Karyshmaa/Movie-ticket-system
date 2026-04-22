package com.kary.moviebooking.service.Impl;

import com.kary.moviebooking.dto.SignupRequestDTO;
import com.kary.moviebooking.entity.User;
import com.kary.moviebooking.entity.VerificationToken;
import com.kary.moviebooking.enums.Role;
import com.kary.moviebooking.repository.UserRepository;
import com.kary.moviebooking.repository.VerificationTokenRepository;
import com.kary.moviebooking.service.Interface.Authservice;
import com.kary.moviebooking.service.Interface.EmailService;
import com.kary.moviebooking.service.Interface.VerificationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements Authservice {

    private final UserRepository userRepository;
    private final VerificationTokenService tokenService;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    public AuthServiceImpl(UserRepository userRepository,
                           VerificationTokenService tokenService,
                           EmailService emailService,
                           PasswordEncoder passwordEncoder,
    VerificationTokenRepository verificationTokenRepository) {

        this.userRepository = userRepository;
        this.tokenService = tokenService;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
        this.verificationTokenRepository = verificationTokenRepository;
    }

    @Override
    public String signup(SignupRequestDTO request) {

        User user = new User();
        user.setName(request.getName()); // make sure DTO has this
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.ROLE_USER);
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
}
