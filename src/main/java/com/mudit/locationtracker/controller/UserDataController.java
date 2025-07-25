package com.mudit.locationtracker.controller;

import com.mudit.locationtracker.dto.FeatureDTO;
import com.mudit.locationtracker.dto.UserDTO;
import com.mudit.locationtracker.service.FriendService;
import com.mudit.locationtracker.service.FeatureService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserDataController {

    private final FriendService friendService;
    private final FeatureService featureService;

    @GetMapping("/{phone}/friends")
    public List<UserDTO> getFriends(@PathVariable String phone) {
        return friendService.getFriendsOf(phone);
    }

    @GetMapping("/{phone}/features")
    public List<FeatureDTO> getApprovedFeatures(@PathVariable String phone) {
        return featureService.getApprovedFeaturesForTrackee(phone);
    }

    @GetMapping("/{phone}/friends/features")
    public Map<String, List<FeatureDTO>> getFriendsWithFeatures(@PathVariable String phone) {
        return friendService.getFriendsOf(phone).stream()
                .collect(Collectors.toMap(
                        UserDTO::getPhoneNumber,
                        friend -> featureService.getApprovedFeaturesForTrackee(friend.getPhoneNumber())
                ));
    }
}