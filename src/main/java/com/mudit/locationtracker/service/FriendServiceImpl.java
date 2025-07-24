package com.mudit.locationtracker.service;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FriendServiceImpl implements FriendService{
    public List<String> getFriendsOf(String userId) {
        return List.of("device123", "friend2", "friend3"); // Replace with DB fetch
    }
}
