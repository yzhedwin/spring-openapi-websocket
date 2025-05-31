package com.demo.openapi.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Controller;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class RestAPIGateway {

    private final MessageChannel apiRequestChannelScheduleUpdated;

    public RestAPIGateway(
            @Qualifier("apiRequestChannelScheduleUpdated") MessageChannel apiRequestChannelScheduleUpdated) {
        this.apiRequestChannelScheduleUpdated = apiRequestChannelScheduleUpdated;
        log.info("RestAPIGateway initialized with channel: " + apiRequestChannelScheduleUpdated);
    }

    public void send(Object event) {
        log.info("Sending event to API");
        apiRequestChannelScheduleUpdated.send(MessageBuilder.withPayload(event).build());
    }

}
