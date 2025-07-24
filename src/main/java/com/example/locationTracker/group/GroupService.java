package com.example.locationTracker.group;

import com.example.locationTracker.repository.GroupLocationShareRepository;
import com.example.locationTracker.repository.GroupRepository;
import com.example.locationTracker.repository.UserRepository;
import com.example.locationTracker.user.UserEntity;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final GroupLocationShareRepository groupLocationShareRepository;

    /**
     * Create a new group with one admin (identified by phone number).
     */
    public GroupEntity createGroup(String name, String adminPhone) {
        UserEntity admin = userRepository.findById(adminPhone)
                .orElseThrow(() -> new EntityNotFoundException("Admin user not found: " + adminPhone));

        GroupEntity group = GroupEntity.builder()
                .name(name)
                .build();

        group.addAdmin(admin);
        group.addMember(admin);

        return groupRepository.save(group); // Hibernate generates UUID automatically
    }

    @Transactional
    public void addMember(UUID groupId, String userPhone) {
        GroupEntity group = groupRepository.findById(groupId)
                .orElseThrow(() -> new EntityNotFoundException("Group not found: " + groupId));
        UserEntity user = userRepository.findById(userPhone)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + userPhone));

        group.addMember(user);
        groupRepository.save(group);
    }

    @Transactional
    public void addAdmin(UUID groupId, String userPhone) {
        GroupEntity group = groupRepository.findById(groupId)
                .orElseThrow(() -> new EntityNotFoundException("Group not found: " + groupId));
        UserEntity user = userRepository.findById(userPhone)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + userPhone));

        group.addAdmin(user);
        groupRepository.save(group);
    }

    public List<GroupEntity> getAllGroups() {
        return groupRepository.findAll();
    }

    public GroupEntity getGroup(UUID groupId) {
        return groupRepository.findById(groupId)
                .orElseThrow(() -> new EntityNotFoundException("Group not found: " + groupId));
    }

    @Transactional
    public GroupLocationShareEntity shareLocation(String userPhone, UUID groupId) {
        UserEntity user = userRepository.findById(userPhone)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + userPhone));
        GroupEntity group = groupRepository.findById(groupId)
                .orElseThrow(() -> new EntityNotFoundException("Group not found: " + groupId));

        return groupLocationShareRepository.save(
                GroupLocationShareEntity.builder()
                        .group(group)
                        .user(user)
                        .active(true)
                        .build()
        );
    }

    @Transactional
    public void stopSharingLocation(String userPhone, UUID groupId) {
        UserEntity user = userRepository.findById(userPhone)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + userPhone));
        GroupEntity group = groupRepository.findById(groupId)
                .orElseThrow(() -> new EntityNotFoundException("Group not found: " + groupId));

        GroupLocationShareEntity share = groupLocationShareRepository.findByGroupAndUser(group, user)
                .orElseThrow(() -> new EntityNotFoundException("Location share not found"));

        share.setActive(false);
        groupLocationShareRepository.save(share);
    }

    public List<UserEntity> getSharedLocations(UUID groupId) {
        GroupEntity group = groupRepository.findById(groupId)
                .orElseThrow(() -> new EntityNotFoundException("Group not found: " + groupId));

        return groupLocationShareRepository.findByGroupAndActiveTrue(group)
                .stream()
                .map(GroupLocationShareEntity::getUser)
                .toList();
    }

}
