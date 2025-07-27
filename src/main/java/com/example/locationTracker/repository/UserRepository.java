package com.example.locationTracker.repository;

import com.example.locationTracker.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, String> {
    List<UserEntity> findAllByPhoneNumberIn(List<String> phoneNumbers);
}