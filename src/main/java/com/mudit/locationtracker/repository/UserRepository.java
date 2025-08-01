package com.mudit.locationtracker.repository;

import com.mudit.locationtracker.model.UserEntity;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {

    @Query("SELECT u FROM UserEntity u LEFT JOIN FETCH u.friends WHERE u.phoneNumber = :phone_number")
    Optional<UserEntity> fetchWithFriends(String phone_number);

    @Query("SELECT DISTINCT u FROM UserEntity u LEFT JOIN FETCH u.friends")
    List<UserEntity> fetchAllUsers();
}