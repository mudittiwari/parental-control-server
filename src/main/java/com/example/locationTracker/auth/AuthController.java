package com.example.locationTracker.auth;

import com.example.locationTracker.dto.UserDTO;
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
    public UserDTO login(@RequestBody LoginRequest request) {
        return userService.loginUser(request.getPhoneNumber(), request.getPassword());
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
