package com.example.locationTracker.group;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupDTO {
    private UUID id;
    private String name;
    private List<String> members; // phone numbers or names
    private List<String> admins;

    public static GroupDTO fromEntity(GroupEntity group) {
        return GroupDTO.builder()
                .id(group.getId())
                .name(group.getName())
                .members(group.getMembers().stream().map(u -> u.getPhoneNumber()).toList())
                .admins(group.getAdmins().stream().map(u -> u.getPhoneNumber()).toList())
                .build();
    }
}
