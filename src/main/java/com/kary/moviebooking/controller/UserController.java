package com.kary.moviebooking.controller;

import com.kary.moviebooking.entity.User;
import com.kary.moviebooking.exception.ResourceNotFoundException;
import com.kary.moviebooking.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //Create user
    @PostMapping
    public User createUser(@RequestBody User user) {
        user.setCreatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    //Get user by id
    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));
    }

    //Get all users
    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
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