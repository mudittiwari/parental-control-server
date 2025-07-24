package com.example.locationTracker.controller;

import com.example.locationTracker.group.GroupDTO;
import com.example.locationTracker.group.GroupEntity;
import com.example.locationTracker.group.GroupService;
import com.example.locationTracker.user.UserEntity;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    @PostMapping
    public GroupDTO createGroup(@RequestBody CreateGroupRequest request) {
        GroupEntity group = groupService.createGroup(request.getName(), request.getAdminPhone());
        return GroupDTO.fromEntity(group);
    }

    @PostMapping("/{groupId}/members/{userPhone}")
    public String addMember(@PathVariable UUID groupId, @PathVariable String userPhone) {
        groupService.addMember(groupId, userPhone);
        return "Member added successfully.";
    }

    @PostMapping("/{groupId}/admins/{userPhone}")
    public String addAdmin(@PathVariable UUID groupId, @PathVariable String userPhone) {
        groupService.addAdmin(groupId, userPhone);
        return "Admin added successfully.";
    }

    @GetMapping
    public List<GroupDTO> getAllGroups() {
        return groupService.getAllGroups()
                .stream()
                .map(GroupDTO::fromEntity)
                .toList();
    }

    @GetMapping("/{groupId}")
    public GroupDTO getGroup(@PathVariable UUID groupId) {
        return GroupDTO.fromEntity(groupService.getGroup(groupId));
    }

    @Data
    public static class CreateGroupRequest {
        private String name;
        private String adminPhone;
    }

    @PostMapping("/{groupId}/share-location")
    public String shareLocation(@PathVariable UUID groupId, @RequestParam String userPhone) {
        groupService.shareLocation(userPhone, groupId);
        return "Location sharing started.";
    }

    @DeleteMapping("/{groupId}/share-location/{userPhone}")
    public String stopSharingLocation(@PathVariable UUID groupId, @PathVariable String userPhone) {
        groupService.stopSharingLocation(userPhone, groupId);
        return "Location sharing stopped.";
    }

    @GetMapping("/{groupId}/locations")
    public List<UserEntity> getSharedLocations(@PathVariable UUID groupId) {
        return groupService.getSharedLocations(groupId);
    }
}
