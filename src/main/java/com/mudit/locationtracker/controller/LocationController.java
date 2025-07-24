package com.mudit.locationtracker.controller;
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

    public LocationController(SimpMessagingTemplate simpMessagingTemplate){
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @MessageMapping("/send-location")
    @SendTo("/topic/location")
    public LocationData handleLocation(LocationData location) {
        System.out.println("Received: " + location.getSenderId() + " -> " + location.getReceiverId());

        List<String> friends = friendService.getFriendsOf(location.getSenderId());

        for (int i = 0; i < friends.size(); i++) {
            String friendId = friends.get(i);
            int delaySeconds = 10;
            CompletableFuture.delayedExecutor(delaySeconds, TimeUnit.SECONDS)
                    .execute(() -> {
                        NotificationData notif = new NotificationData();
                        notif.setUserId(friendId);
                        notif.setTitle("Location Update");
                        notif.setMessage("Your friend " + location.getSenderId() + " updated their location.");

                        simpMessagingTemplate.convertAndSend("/topic/notifications/" + friendId, notif);
                        System.out.println("Sent delayed notification to: " + friendId);
                    });
        }
        return location;
    }

}
