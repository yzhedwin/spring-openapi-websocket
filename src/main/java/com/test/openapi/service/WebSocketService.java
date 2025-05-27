package com.test.openapi.service;


import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.test.openapi.model.EMessageType;
import com.test.openapi.model.TestMessage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@ComponentScan
@Slf4j
@RequiredArgsConstructor
public class WebSocketService {
 
    private final SimpMessageSendingOperations messagingTemplate;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        log.info("Received a new web socket connection");
    }
    
    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = null;
        var sessionAttributes = headerAccessor.getSessionAttributes();
        if (sessionAttributes != null) {
            username = (String) sessionAttributes.get("username");
        }
        
        if (username != null) {
            log.info("user disconnected: {}", username);
            
            var chatMessage = TestMessage.builder()
                    .type(EMessageType.LEAVE)
                    .sender(username)
                    .build();
            
            messagingTemplate.convertAndSend("/topic/public", chatMessage);
        }
    }
    
}