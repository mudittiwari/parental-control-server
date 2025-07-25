package com.mudit.locationtracker.service;
import com.mudit.locationtracker.dto.FeatureDTO;
import com.mudit.locationtracker.dto.UserDTO;

import java.util.List;

public interface FriendService {
    public List<UserDTO> getFriendsOf(String userId) ;
}
