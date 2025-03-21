package com.example.book_catalog.controller;

import com.example.book_catalog.model.User;
import com.example.book_catalog.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")  // Added base URL mapping
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Show the registration page
    @GetMapping("/register")
    public String showRegisterPage() {
        return "register"; // Ensure register.html exists in templates
    }

    // Handle user registration (POST request)
    @PostMapping("/register")
    public String registerUser(@RequestParam String username, @RequestParam String password, Model model) {
        // Check if username already exists
        if (userRepository.findByUsername(username).isPresent()) {
            model.addAttribute("error", "Username already exists!");
            return "register";
        }

        // Save user with encrypted password
        User user = new User();
        user.setUsername(username); // Fixed: setUsername instead of setUserName
        user.setPassword(passwordEncoder.encode(password)); // Hash the password
        user.setRole("USER"); // Default role

        userRepository.save(user);
        System.out.println("in auth");
        return "redirect:/auth/login"; // Redirect to login page after registration
    }

    // Show the login page
    @GetMapping("/login")
    public String showLoginPage() {
        return "login"; // Ensure login.html exists
    }
}
