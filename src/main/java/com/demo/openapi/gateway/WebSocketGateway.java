package com.demo.openapi.gateway;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Profile("server")
@Slf4j
@Component
public class WebSocketGateway implements GatewayInterface {
    private final MessageChannel transmitToWebSocket;

    public WebSocketGateway(@Qualifier("transmitToWebSocket") MessageChannel transmitToWebSocket) {
        this.transmitToWebSocket = transmitToWebSocket;
    }

    @Override
    public void send(Object event) {
        // Send via WebSocket
        System.out.println("Sending event to WebSocket");
        transmitToWebSocket.send(MessageBuilder.withPayload(event).build());
    }
}