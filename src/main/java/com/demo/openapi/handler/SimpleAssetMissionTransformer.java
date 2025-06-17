package com.demo.openapi.handler;

import org.springframework.integration.core.GenericTransformer;

import com.demo.common.model.SimpleAssetMission;

public class SimpleAssetMissionTransformer implements GenericTransformer<SimpleAssetMission, Object> {

    @Override
    public Object transform(SimpleAssetMission source) {
        return MissionMapperInterface.INSTANCE.toScheduledTask(source);
    }
}
