package com.mudit.locationtracker.service;

import com.mudit.locationtracker.dto.UserDTO;
import com.mudit.locationtracker.model.UserEntity;
import com.mudit.locationtracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FriendServiceImpl implements FriendService {

    private final UserRepository userRepository;

    @Override
    public List<UserDTO> getFriendsOf(String userPhone) {
        // UserEntity user = userRepository.fetchWithFriends(userPhone)
        // .orElseThrow(() -> new RuntimeException("User not found"));

        // return user.getFriends().stream()
        // .map(UserDTO::fromEntity)
        // .collect(Collectors.toList());

        List<UserEntity> users = userRepository.fetchAllUsers();

        return users.stream()
                .map(UserDTO::fromEntity)
                .collect(Collectors.toList());
    }
}
