package com.mudit.locationtracker.listener;

import java.security.Principal;
import java.util.Map;

import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;

import com.mudit.locationtracker.controller.LocationController;

@Component
public class StompConnectListener implements ApplicationListener<SessionConnectedEvent> {

    private final Map<String, String> phoneToSessionMap = LocationController.phoneToSessionMap;

    @Override
    public void onApplicationEvent(SessionConnectedEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = accessor.getSessionId();
        Principal user = accessor.getUser();
        final String phone = (user != null) ? user.getName() : null;
        if (phone != null) {
            if(phoneToSessionMap.entrySet().removeIf(e -> e.getKey().equals(phone))){
                System.out.println("old session removed successfully");
            }
            phoneToSessionMap.put(phone, sessionId);
            System.out.println("Connected: " + phone + " -> " + sessionId);
        } else {
            System.out.println("phoneNumber not found in session attributes");
        }
    }
}
