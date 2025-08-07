package com.mudit.locationtracker.service;

import org.springframework.stereotype.Service;

import com.mudit.locationtracker.model.UserEntity;
import com.mudit.locationtracker.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public String getUserToken(String phoneNumber) {
        return userRepository.findById(phoneNumber)
                .map(UserEntity::getToken)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }
}
