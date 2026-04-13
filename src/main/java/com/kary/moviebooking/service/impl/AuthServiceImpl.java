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
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEnabled(true); // skip verification for now
        user.setRole(Role.ROLE_USER);
        user.setUsername(request.getEmail());

        userRepository.save(user);

        return "Signup successful";
    }

        public String verify(String token) {
            System.out.println("VERIFY API CALLED");

            VerificationToken vt = verificationTokenRepository.findByToken(token)
                    .orElseThrow(() -> new RuntimeException("Invalid token"));

            User user = vt.getUser();
            user.setEnabled(true);

            user.setId(user.getId());
            userRepository.save(user);

            return "Email verified successfully";
    }
}
