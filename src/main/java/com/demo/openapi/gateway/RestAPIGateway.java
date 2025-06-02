package com.demo.openapi.gateway;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Profile({"client", "test"})
@Component
public class RestAPIGateway implements GatewayInterface {

    private final MessageChannel apiRequestChannelScheduleUpdated;

    public RestAPIGateway(
            @Qualifier("apiRequestChannelScheduleUpdated") MessageChannel apiRequestChannelScheduleUpdated) {
        this.apiRequestChannelScheduleUpdated = apiRequestChannelScheduleUpdated;
        log.info("RestAPIGateway initialized with channel: " + apiRequestChannelScheduleUpdated);
    }

    @Override
    public void send(Object event) {
        log.info("Sending event to API");
        apiRequestChannelScheduleUpdated.send(MessageBuilder.withPayload(event).build());
    }

}
