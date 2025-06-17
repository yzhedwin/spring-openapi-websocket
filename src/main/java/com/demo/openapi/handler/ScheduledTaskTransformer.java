package com.demo.openapi.handler;

import org.springframework.integration.core.GenericTransformer;

import com.demo.common.model.ScheduledTask;

public class ScheduledTaskTransformer implements GenericTransformer<ScheduledTask, Object> {

    @Override
    public Object transform(final ScheduledTask source) {
        return MissionMapperInterface.INSTANCE.toSimpleAssetMission(source);
    }
}
