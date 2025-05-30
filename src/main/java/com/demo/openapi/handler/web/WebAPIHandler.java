package com.demo.openapi.handler.web;

import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import com.demo.openapi.service.WebService;

@Service
public class WebAPIHandler {
  private final WebService webService;

  public WebAPIHandler(WebService webService) {
    this.webService = webService;
  }

  @ServiceActivator(inputChannel = "apiRequestChannelScheduleUpdated")
  public void apiRequestHandler(Message<?> message) {
    System.out.println("Received message in WebAPIHandler: " + message);
    // Process the message as needed
    webService.postRequest(message, "api/mmi/schedule")
        .doOnSuccess(response -> System.out.println("Response: " + response))
        .onErrorResume(error -> {
          System.err.println("Error occurred: " + error.getMessage());
          return null; // Handle the error appropriately
        })
        .subscribe();
  }

  // @ServiceActivator(inputChannel = "apiRequestChannel")
  // @Bean
  // public WebFluxRequestExecutingMessageHandler reactiveOutbound(WebClient
  // client) {
  // WebFluxRequestExecutingMessageHandler handler = new
  // WebFluxRequestExecutingMessageHandler(
  // config.getBaseUrl() + "/foo", client);
  // handler.setHttpMethod(HttpMethod.POST);
  // handler.setExpectedResponseType(String.class);
  // return handler;
  // }
}
