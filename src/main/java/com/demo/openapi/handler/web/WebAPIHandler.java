package com.demo.openapi.handler.web;

import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Service;

import com.demo.openapi.model.GenericMessageModel;
import com.demo.openapi.model.SchedulerModel;
import com.demo.openapi.service.WebService;

@Service
public class WebAPIHandler {
  WebService webService;

  public WebAPIHandler(WebService webService) {
    this.webService = webService;
  }

  @ServiceActivator(inputChannel = "apiRequestChannel")
  public void apiRequestHandler(GenericMessageModel message) {
    // Process the message as needed
    switch (message) {
      case SchedulerModel schedulerModel -> {
        webService.postSchedulerChange(schedulerModel)
            .doOnSuccess(response -> System.out.println("Response: " + response))
            .subscribe();
      }
      default -> System.out.println("Received message in WebClientHandler: " + message);
    }
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
