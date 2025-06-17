package com.demo.openapi.gateway;

import java.util.List;

import com.demo.common.model.SimpleAssetMission;
import com.demo.openapi.model.EHttp;

public interface GatewayInterface {
    public void send(Object event, EHttp type);

    public List<SimpleAssetMission> receive();

    public void send(Object event);
}
