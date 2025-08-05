package com.mudit.locationtracker.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ConnectionStatusController {

    @GetMapping("/is-connected/{phone}")
    public boolean isConnected(@PathVariable String phone) {
        return LocationController.phoneToSessionMap.containsKey(phone);
    }
}
