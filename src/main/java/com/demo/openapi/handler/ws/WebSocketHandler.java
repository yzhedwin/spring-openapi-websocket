package com.demo.openapi.handler.ws;

import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

@Service
public class WebSocketHandler {

    // This class can be used to handle WebSocket messages, similar to the
    // WebAPIHandler
    // You can implement methods to handle incoming messages, send messages, etc.
    // For example, you might want to implement a method to handle incoming
    // WebSocket messages
    // and another method to send messages over the WebSocket connection.

    // Example method to handle incoming messages
    public void handleIncomingMessage(Message<?> message) {
        System.out.println(">>>> Incoming payload: " + message.getPayload());
        // System.out.println(">>>> Headers: " + message.getHeaders()); // Process the
        // message as needed
    }

    // Example method to send a message
    public void sendMessage(String message) {
        System.out.println("Sending message: " + message);
        // Logic to send the message over the WebSocket connection
    }
}
