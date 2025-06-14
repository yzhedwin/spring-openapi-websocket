package com.demo.openapi.handler.ws;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Component
@Profile("server")
@Slf4j
@Getter
public class WebSocketServerMessageHandler implements WebSocketHandler {

    private final Set<WebSocketSession> sessions = ConcurrentHashMap.newKeySet();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.add(session);
        log.info("Client connected: {}", session.getId());
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws IOException {
        log.info("Received message: {}", message.getPayload());
        // Echo back the same message
        session.sendMessage(new TextMessage("Echo: " + message.getPayload()));
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        log.error("WebSocket error on session {}: {}", session.getId(), exception.getMessage());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session);
        log.info("Client disconnected: {}", session.getId());
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    public void sendMessageToAll(Object message) {
        final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
        try {
            for (WebSocketSession session : sessions) {
                session.sendMessage(new TextMessage(mapper.writeValueAsString(message)));
            }
        } catch (IOException e) {
            log.error("Error serializing message: {}", e.getMessage());
        }
    }
}
