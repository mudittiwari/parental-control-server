package com.mudit.locationtracker.controller;

import com.mudit.locationtracker.dto.UserDTO;
import com.mudit.locationtracker.model.LocationData;
import com.mudit.locationtracker.model.NotificationData;
import com.mudit.locationtracker.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Controller
public class LocationController {

    @Autowired
    private FriendService friendService;
    private SimpMessagingTemplate simpMessagingTemplate;

    public LocationController(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @MessageMapping("/send-location")
    public LocationData handleLocation(LocationData location) {
        System.out.println("📥 Received location from: " + location.getSenderId() +
                " -> to: " + location.getReceiverId());

        String receiverId = location.getReceiverId();

        // Schedule the message to be sent after a short delay (e.g., 10 seconds)
        int delaySeconds = 5;
        CompletableFuture.delayedExecutor(delaySeconds, TimeUnit.SECONDS)
                .execute(() -> {
                    NotificationData notif = new NotificationData();
                    notif.setUserId(location.getSenderId());
                    notif.setTitle("📍 Location Update");
                    notif.setMessage("You received a new location from " + location.getSenderId());
                    notif.setPayload(location.getPayload());

                    simpMessagingTemplate.convertAndSend(
                            "/topic/notifications/" + receiverId, notif);

                    System.out.println("📤 Sent location to receiver: " + receiverId);
                });

        return location; // optional: only needed if you still want to return something to sender
    }

}
