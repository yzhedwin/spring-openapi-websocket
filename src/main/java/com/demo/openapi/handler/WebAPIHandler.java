package com.demo.openapi.handler;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.webflux.outbound.WebFluxRequestExecutingMessageHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.demo.openapi.config.WebClientConfig;
import com.demo.openapi.model.GenericMessageModel;
import com.demo.openapi.model.SchedulerModel;
import com.demo.openapi.service.WebService;

@Service
public class WebAPIHandler {
  WebClientConfig config;
  WebService webService;

  public WebAPIHandler(WebClientConfig config, WebService webService) {
    this.config = config;
    this.webService = webService;
  }

  @ServiceActivator(inputChannel = "apiRequestChannel")
  @Bean
  public WebFluxRequestExecutingMessageHandler reactiveOutbound(WebClient client) {
    WebFluxRequestExecutingMessageHandler handler = new WebFluxRequestExecutingMessageHandler(
        config.getBaseUrl() + "/foo", client);
    handler.setHttpMethod(HttpMethod.POST);
    handler.setExpectedResponseType(String.class);
    return handler;
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
}
