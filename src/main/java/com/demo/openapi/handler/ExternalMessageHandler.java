package com.demo.openapi.handler;

import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

@Service
public class ExternalMessageHandler {

  @ServiceActivator(inputChannel = "webSocketChannel")
  public void webSocketMessageHandler(Message<?> message) {
    System.out.println("Received message in external message handler A: " + message.getPayload());
    // Process the message as needed
  }
}
