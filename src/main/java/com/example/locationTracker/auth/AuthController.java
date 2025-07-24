package com.example.locationTracker.auth;

import com.example.locationTracker.user.UserDTO;
import com.example.locationTracker.user.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public UserDTO register(@RequestBody RegisterRequest request) {
        return userService.registerUser(
                request.getPhoneNumber(),
                request.getName(),
                request.getEmail(),
                request.getPassword(),
                request.getLat(),
                request.getLon()
        );
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request) {
        boolean success = userService.loginUser(request.getPhoneNumber(), request.getPassword());
        return success ? "Login successful" : "Invalid phone number or password";
    }

    @Data
    public static class RegisterRequest {
        private String phoneNumber;
        private String name;
        private String email;
        private String password;
        private double lat;
        private double lon;
    }

    @Data
    public static class LoginRequest {
        private String phoneNumber;
        private String password;
    }
}
