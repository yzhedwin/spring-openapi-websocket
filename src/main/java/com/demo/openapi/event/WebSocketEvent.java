package com.demo.openapi.event;

import org.springframework.context.ApplicationEvent;

public class WebSocketEvent extends ApplicationEvent {

    private final String message;

    public WebSocketEvent(Object source, String message) {
        super(source);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
