package com.mudit.locationtracker.dto;

import com.mudit.locationtracker.model.UserEntity;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private String phoneNumber;
    private String name;
    private String email;
    private String pKey;
    private String token;

    private List<String> friendPhones;

    public static UserDTO fromEntity(UserEntity user) {
        return UserDTO.builder()
                .phoneNumber(user.getPhoneNumber())
                .name(user.getName())
                .email(user.getEmail())
                .friendPhones(user.getFriends()
                        .stream()
                        .map(UserEntity::getPhoneNumber)
                        .collect(Collectors.toList()))
                .token(user.getToken())
                .pKey(user.getPKey())
                .build();
    }
}
