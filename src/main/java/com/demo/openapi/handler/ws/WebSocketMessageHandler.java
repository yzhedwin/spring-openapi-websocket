package com.demo.openapi.handler.ws;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.demo.openapi.event.WebSocketEvent;

import lombok.extern.slf4j.Slf4j;

@Profile({"client", "test"})
@Component
@Slf4j
public class WebSocketMessageHandler {

    private final ApplicationEventPublisher publisher;

    public WebSocketMessageHandler(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    public void handle(Object payload) {
        try {
            String json = payload.toString();
            log.info(">>>> Incoming payload: " + json);
            publisher.publishEvent(new WebSocketEvent(this, json));
        } catch (Exception e) {
            log.error("Error handling WebSocket message: " + e.getMessage());
        }
    }
}
