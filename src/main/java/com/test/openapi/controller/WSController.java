package com.test.openapi.controller;

import java.util.Map;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import com.test.openapi.model.TestMessage;

@Controller
public class WSController {
    @MessageMapping("/sendMessage")
    @SendTo("/topic/public")
    public TestMessage sendMessage(@Payload TestMessage chatMessage) {
        
        return chatMessage;
        
    }

    @MessageMapping("/addUser")
    @SendTo("/topic/public")
    public TestMessage addUser(@Payload TestMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        
        // Add username in web socket session
        Map<String, Object> sessionAttributes = headerAccessor.getSessionAttributes();
        if (sessionAttributes != null) {
            sessionAttributes.put("username", chatMessage.getSender());
        }
        return chatMessage;
        
    }
}
