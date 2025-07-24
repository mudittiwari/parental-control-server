package com.example.locationTracker.controller;

import com.example.locationTracker.user.UserDTO;
import com.example.locationTracker.user.UserEntity;
import com.example.locationTracker.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserDTO createUser(@RequestBody UserEntity userRequest) {
        return userService.createUser(
                userRequest.getPhoneNumber(),
                userRequest.getName(),
                userRequest.getEmail(),
                Double.parseDouble(userRequest.getLocation().getLatitude()),
                Double.parseDouble(userRequest.getLocation().getLongitude())
        );
    }

    @GetMapping
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{phoneNumber}")
    public UserDTO getUser(@PathVariable String phoneNumber) {
        return userService.getUser(phoneNumber);
    }

    @PutMapping("/{phoneNumber}/location")
    public UserDTO updateLocation(@PathVariable String phoneNumber,
                                  @RequestBody LocationRequest locationRequest) {
        return userService.updateLocation(phoneNumber, locationRequest.getLat(), locationRequest.getLon());
    }

    @PostMapping("/{phoneNumber}/friends/{friendPhone}")
    public String addFriend(@PathVariable String phoneNumber, @PathVariable String friendPhone) {
        userService.addFriend(phoneNumber, friendPhone);
        return "Friend added.";
    }

    // DTO for location update
    public static class LocationRequest {
        private double lat;
        private double lon;

        public double getLat() { return lat; }
        public void setLat(double lat) { this.lat = lat; }

        public double getLon() { return lon; }
        public void setLon(double lon) { this.lon = lon; }
    }
}
