package com.demo.openapi.event;

import org.springframework.context.ApplicationEvent;

public class APIEvent extends ApplicationEvent {

    private final String message;

    public APIEvent(Object source, String message) {
        super(source);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}