package com.kary.moviebooking.service.Impl;

import com.kary.moviebooking.entity.User;
import com.kary.moviebooking.entity.VerificationToken;
import com.kary.moviebooking.enums.Role;
import com.kary.moviebooking.repository.UserRepository;
import com.kary.moviebooking.service.Interface.Authservice;
import com.kary.moviebooking.service.Interface.EmailService;
import com.kary.moviebooking.service.Interface.VerificationTokenService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements Authservice {

    private final UserRepository userRepository;
    private final VerificationTokenService tokenService;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(UserRepository userRepository,
                           VerificationTokenService tokenService,
                           EmailService emailService,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.tokenService = tokenService;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void signup() {

        // TEMP (we will replace with DTO later)
        User user = new User();
        user.setEmail("test@gmail.com");
        user.setPassword(passwordEncoder.encode("1234"));
        user.setEnabled(false);
        user.setRole(Role.ROLE_USER);

        userRepository.save(user);

        // create token
        VerificationToken token = tokenService.createToken(user);

        // send email
        String link = "http://localhost:8080/auth/verify?token=" + token.getToken();

        emailService.sendEmail(
                user.getEmail(),
                "Verify your account",
                "Click this link: " + link
        );
    }
}