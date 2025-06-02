package com.demo.openapi.handler.web;

import org.springframework.context.annotation.Profile;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import com.demo.openapi.config.web.WebClientConfig;
import com.demo.openapi.service.WebService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Profile({"client", "test"})
public class WebAPIHandler {
  private final WebClientConfig config;
  private final WebService webService;

  public WebAPIHandler(WebService webService, WebClientConfig config) {
    this.config = config;
    this.webService = webService;
  }

  @ServiceActivator(inputChannel = "apiRequestChannelScheduleUpdated")
  public void apiRequestHandlerForScheduleUpdate(Message<?> message) {
    log.info("Received message in WebAPIHandler: " + message);
    // Process the message as needed
    webService.postRequest(message, config.getEndpointSchedule())
        .doOnSuccess(response -> log.info("Response: " + response))
        .onErrorResume(error -> {
          log.error("Failed to send request: ");
          return null; // Handle the error appropriately
        })
        .subscribe();
  }
}
