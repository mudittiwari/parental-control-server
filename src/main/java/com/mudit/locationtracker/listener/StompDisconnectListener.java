package com.mudit.locationtracker.listener;

import java.util.Map;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.mudit.locationtracker.controller.LocationController;

@Component
public class StompDisconnectListener implements ApplicationListener<SessionDisconnectEvent> {

    private final Map<String, String> phoneToSessionMap = LocationController.phoneToSessionMap;

    @Override
    public void onApplicationEvent(SessionDisconnectEvent event) {
        String sessionId = event.getSessionId();
        phoneToSessionMap.entrySet().removeIf(entry -> entry.getValue().equals(sessionId));
        System.out.println("Disconnected: " + sessionId);
    }
}

