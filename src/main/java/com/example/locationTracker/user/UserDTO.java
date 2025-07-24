package com.example.locationTracker.user;

import com.example.locationTracker.location.LocationEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private String phoneNumber;
    private String name;
    private String email;
    private LocationEntity location;
    private List<String> friends;

    public static UserDTO fromEntity(UserEntity user) {
        return UserDTO.builder()
                .phoneNumber(user.getPhoneNumber())
                .name(user.getName())
                .email(user.getEmail())
                .location(user.getLocation())
                .friends(
                        user.getFriends()
                                .stream()
                                .map(UserEntity::getPhoneNumber)
                                .collect(Collectors.toList())
                )
                .build();
    }
}
