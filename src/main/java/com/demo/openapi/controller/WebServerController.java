package com.demo.openapi.controller;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.demo.openapi.event.APIEvent;

import lombok.extern.slf4j.Slf4j;

@Profile("server")
@RestController
@Slf4j
public class WebServerController {

    private final ApplicationEventPublisher publisher;

    public WebServerController(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @PostMapping("/api/mmi/schedule")
    public ResponseEntity<String> postSchedule(@RequestBody String payload) {
        try {
            log.info(">>>> Incoming payload: {}", payload);
            publisher.publishEvent(new APIEvent(this, payload));
            return ResponseEntity.ok("Event published successfully");
        } catch (Exception e) {
            log.error("Error handling request: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to publish event");
        }
    }
}
