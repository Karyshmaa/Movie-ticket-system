package com.kary.moviebooking.controller;

import com.kary.moviebooking.entity.User;
import com.kary.moviebooking.repository.UserRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestUserController {
    private final UserRepository userRepository;

    public TestUserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/test-user")
    public String testUser() {
        User user = new User();
        user.setName("Kary");
        user.setEmail("kary@test.com");
        user.setPassword("12345");

        userRepository.save(user);

        return "User saved!";
    }
}
