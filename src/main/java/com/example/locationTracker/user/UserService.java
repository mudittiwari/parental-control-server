package com.example.locationTracker.user;

import com.example.locationTracker.dto.UserDTO;
import com.example.locationTracker.feature.FeatureService;
import com.example.locationTracker.location.LocationEntity;
import com.example.locationTracker.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final FeatureService featureService;
    private final PasswordEncoder passwordEncoder;

    public UserDTO registerUser(String phoneNumber, String name, String email, String password, double lat, double lon, String pKey) {
        log.info("Registering user: phone={}, name={}, email={}", phoneNumber, name, email);

        UserEntity user = UserEntity.builder()
                .phoneNumber(phoneNumber)
                .name(name)
                .email(email)
                .password(passwordEncoder.encode(password)) // HASHING PASSWORD
                .location(new LocationEntity(null, String.valueOf(lat), String.valueOf(lon)))
                .pKey(pKey)
                .build();

        UserEntity saved = userRepository.save(user);
        return UserDTO.fromEntity(saved);
    }

    public UserDTO loginUser(String phoneNumber, String rawPassword) {
        UserEntity user = userRepository.findById(phoneNumber)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new IllegalArgumentException("Invalid password");
        }

        return UserDTO.fromEntity(user);
    }

    public UserDTO getUser(String phoneNumber) {
        return UserDTO.fromEntity(userRepository.findById(phoneNumber)
                .orElseThrow(() -> new EntityNotFoundException("User not found")));
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream().map(UserDTO::fromEntity).collect(Collectors.toList());
    }

    public UserDTO createUser(String phoneNumber, String name, String email, double lat, double lon) {
        log.info("Creating user: phone={}, name={}, email={}, lat={}, lon={}",
                phoneNumber, name, email, lat, lon);

        UserEntity user = UserEntity.builder()
                .phoneNumber(phoneNumber)
                .name(name)
                .email(email)
                .location(new LocationEntity(null, String.valueOf(lat), String.valueOf(lon)))
                .build();

        UserDTO savedUser = UserDTO.fromEntity(userRepository.save(user));
        log.info("User created successfully: {}", savedUser);
        return savedUser;
    }

    @Transactional
    public UserDTO updateLocation(String phoneNumber, double lat, double lon) {
        log.info("Updating location for user={} to lat={}, lon={}", phoneNumber, lat, lon);

        UserEntity user = userRepository.findById(phoneNumber)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (user.getLocation() == null) {
            user.setLocation(new LocationEntity(null, String.valueOf(lat), String.valueOf(lon)));
        } else {
            user.getLocation().setLatitude(String.valueOf(lat));
            user.getLocation().setLongitude(String.valueOf(lon));
        }

        UserEntity updatedUser = userRepository.save(user);
        featureService.onTrackeeLocationUpdate(updatedUser);

        return UserDTO.fromEntity(updatedUser);
    }

    public List<UserDTO> findUsersByPhoneNumbers(List<String> phoneNumbers) {
        List<UserEntity> users = userRepository.findAllByPhoneNumberIn(phoneNumbers);
        return users.stream().map(UserDTO::fromEntity).toList();
    }

    @Transactional
    public void addFriend(String userPhone, String friendPhone) {
        log.info("Adding friend: user={} friend={}", userPhone, friendPhone);

        UserEntity user = userRepository.findById(userPhone)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        UserEntity friend = userRepository.findById(friendPhone)
                .orElseThrow(() -> new EntityNotFoundException("Friend not found"));

        if (!user.getFriends().contains(friend)) {
            user.getFriends().add(friend);
            log.info("Added {} as a friend to {}", friendPhone, userPhone);
        }
        if (!friend.getFriends().contains(user)) {
            friend.getFriends().add(user);
            log.info("Added {} as a friend to {}", userPhone, friendPhone);
        }

        userRepository.save(user);
        userRepository.save(friend);
        log.info("Friendship saved between {} and {}", userPhone, friendPhone);
    }
}
