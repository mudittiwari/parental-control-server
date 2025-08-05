package com.mudit.locationtracker.interceptor;

import java.util.HashMap;
import java.util.Map;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

import com.mudit.locationtracker.controller.LocationController;
import com.mudit.locationtracker.dto.StompPrincipal;

@Component
public class StompConnectionInterceptor implements ChannelInterceptor {

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String phoneNumber = accessor.getFirstNativeHeader("phoneNumber");
            System.out.println("CONNECT received with phoneNumber = " + phoneNumber);

            if (phoneNumber != null) {
                accessor.setUser(new StompPrincipal(phoneNumber));
                if (LocationController.phoneToSessionMap.containsKey(phoneNumber)) {
                    System.out.println("Duplicate connection attempt for: " + phoneNumber);
                    return null;
                }
            } else {
                System.out.println("No phoneNumber provided, rejecting");
                return null;
            }
        }

        return message;
    }

}
