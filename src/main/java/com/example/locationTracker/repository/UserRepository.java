package com.example.locationTracker.repository;

import com.example.locationTracker.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, String> {
}