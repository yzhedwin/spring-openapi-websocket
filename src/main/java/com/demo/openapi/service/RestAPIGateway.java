package com.demo.openapi.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Service;

@Service
public class RestAPIGateway {

    private final MessageChannel apiRequestChannelScheduleUpdated;

    public RestAPIGateway(
            @Qualifier("apiRequestChannelScheduleUpdated") MessageChannel apiRequestChannelScheduleUpdated) {
        this.apiRequestChannelScheduleUpdated = apiRequestChannelScheduleUpdated;
        System.out.println("RestAPIGateway initialized with channel: " + apiRequestChannelScheduleUpdated);
    }

    public void send(Object event) {
        System.out.println("Sending event to API");
        apiRequestChannelScheduleUpdated.send(MessageBuilder.withPayload(event).build());
    }
}
