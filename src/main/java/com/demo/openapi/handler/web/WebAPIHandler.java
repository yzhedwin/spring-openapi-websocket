package com.demo.openapi.handler.web;

import java.util.List;

import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import com.demo.common.model.ScheduledTask;
import com.demo.common.model.SimpleAssetMission;
import com.demo.openapi.config.web.WebClientConfig;
import com.demo.openapi.service.WebService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class WebAPIHandler {
  private final WebClientConfig config;
  private final WebService webService;

  public WebAPIHandler(WebService webService, WebClientConfig config) {
    this.config = config;
    this.webService = webService;
  }

  public void apiChannelSchedulePatch(ScheduledTask task) {
    if (task.getName() != null) {
      webService
          .patchSimpleAssetMission(config.getEndpointSchedule(), "/updateTitle", task.getId(), task.getName())
          .block();
      return;
    }
    if (task.getStartTime() != null) {
      log.debug("patched start time, {} {}", task.getStartTime(), task.getEndTime());

      webService
          .patchSimpleAssetMission(config.getEndpointSchedule(), "/updateStartTime", task.getId(),
              task.getStartTime().toInstant().toEpochMilli())
          .block();
      if (task.getEndTime() != null) {
        log.debug("patched duration, {} {}", task.getEndTime(), task.getDuration());
        webService
            .patchSimpleAssetMission(config.getEndpointSchedule(), "/updateDuration", task.getId(),
                task.getDuration() * 1000)
            .block();
      }
      return;
    }
  }

  public void apiChannelSchedulePut(SimpleAssetMission mission) {
    webService.putSimpleAssetMission(mission, config.getEndpointSchedule()).block();
  }

  public void apiChannelScheduleDelete(SimpleAssetMission mission) {
    webService.deleteSimpleAssetMission(mission.getId(), config.getEndpointSchedule()).block();
  }

  public List<SimpleAssetMission> apiChannelScheduleGet(Message<?> mission) {
    return webService.getAllSimpleAssetMission(config.getEndpointSchedule()).block();
  }
}
