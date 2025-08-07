package com.mudit.locationtracker.controller;

import com.mudit.locationtracker.dto.UserDTO;
import com.mudit.locationtracker.dto.notification.NotificationRequest;
import com.mudit.locationtracker.model.LocationData;
import com.mudit.locationtracker.model.NotificationData;
import com.mudit.locationtracker.service.FirebaseService;
import com.mudit.locationtracker.service.FriendService;
import com.mudit.locationtracker.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Controller
public class LocationController {

    @Autowired
    private FriendService friendService;

    @Autowired
    private FirebaseService firebaseService;

    @Autowired
    private ConnectionStatusController connectionStatusController;

    @Autowired
    private UserService userService;

    private SimpMessagingTemplate simpMessagingTemplate;
    public static final Map<String, String> phoneToSessionMap = new ConcurrentHashMap<>();

    public LocationController(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @MessageMapping("/send-location")
    public LocationData handleLocation(LocationData location) {

        System.out.println("📥 Received location from: " + location.getSenderId() +
                " -> to: " + location.getReceiverId());

        String receiverId = location.getReceiverId();

        if (connectionStatusController.isConnected(receiverId)) {
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
        }

        else {
            try {
                System.out.println("Sending notification to receiver to turn on notifications");
                System.out.println(userService.getUserToken(location.getSenderId()));
                firebaseService.sendMessageToToken(new NotificationRequest("Alert from " + receiverId, "Please turn on your in-app notifications to receive my location updates",
                        "Alert",
                        userService.getUserToken(location.getSenderId())));
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        return location; // optional: only needed if you still want to return something to sender
    }

}
