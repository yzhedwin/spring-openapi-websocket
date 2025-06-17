package com.demo.openapi.handler;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import com.demo.common.model.ScheduledTask;
import com.demo.common.model.SimpleAssetMission;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface MissionMapperInterface {

    MissionMapperInterface INSTANCE = Mappers.getMapper(MissionMapperInterface.class);

    @Mapping(target = "name", source = "title")
    @Mapping(target = "startTime", expression = "java(mapEpochMsToZonedDateTime(mission.getAbsoluteStartTimeEpochMs()))")
    @Mapping(target = "endTime", expression = "java(calculateEndTime(mission.getAbsoluteStartTimeEpochMs(),mission.getDurationMs()))")
    ScheduledTask toScheduledTask(SimpleAssetMission mission);

    @Mapping(target = "title", source = "name")
    @Mapping(target = "durationMs", expression = "java(mission.getStartTime() == null ? -1 : mission.getEndTime() == null ? -1 : mission.getDuration() * 1000)")
    @Mapping(target = "absoluteStartTimeEpochMs", expression = "java(mission.getStartTime() != null ? mission.getStartTime().toInstant().toEpochMilli() : -1)")
    SimpleAssetMission toSimpleAssetMission(ScheduledTask mission);

    default ZonedDateTime mapEpochMsToZonedDateTime(long epochMS) {
        return Instant.ofEpochMilli(epochMS).atZone(ZoneId.systemDefault());
    }

    default ZonedDateTime calculateEndTime(long absoluteStartTimeEpochMs, long durationMs) {
        return Instant.ofEpochMilli(absoluteStartTimeEpochMs + durationMs)
                .atZone(ZoneId.systemDefault());
    }

}
