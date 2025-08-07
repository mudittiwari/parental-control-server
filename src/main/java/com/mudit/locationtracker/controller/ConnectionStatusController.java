package com.mudit.locationtracker.controller;

import org.springframework.stereotype.Component;

@Component
public class ConnectionStatusController {
    public boolean isConnected(String phone) {
        return LocationController.phoneToSessionMap.containsKey(phone);
    }
}
