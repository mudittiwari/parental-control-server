package com.example.locationTracker.auth;

import com.example.locationTracker.dto.UserDTO;
import com.example.locationTracker.user.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public UserDTO register(@RequestBody RegisterRequest request) throws JsonProcessingException {
        log.info("pKey value is: {}", request.getPKey());
        log.info("Raw JSON: {}", new ObjectMapper().writeValueAsString(request));
        return userService.registerUser(
                request.getPhoneNumber(),
                request.getName(),
                request.getEmail(),
                request.getPassword(),
                request.getLat(),
                request.getLon(),
                request.getPKey()
        );
    }

    @PostMapping("/login")
    public UserDTO login(@RequestBody LoginRequest request) {
        return userService.loginUser(request.getPhoneNumber(), request.getPassword());
    }

    @Data
    public static class LoginRequest {
        private String phoneNumber;
        private String password;
    }
}
