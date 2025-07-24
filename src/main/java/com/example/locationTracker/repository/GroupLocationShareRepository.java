package com.example.locationTracker.repository;

import com.example.locationTracker.group.GroupLocationShareEntity;
import com.example.locationTracker.group.GroupEntity;
import com.example.locationTracker.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GroupLocationShareRepository extends JpaRepository<GroupLocationShareEntity, Long> {
    List<GroupLocationShareEntity> findByGroupAndActiveTrue(GroupEntity group);
    Optional<GroupLocationShareEntity> findByGroupAndUser(GroupEntity group, UserEntity user);
}
