package com.mukminov.service.core;

import com.mukminov.api.generated.dto.RoadmapCreateDto;
import com.mukminov.api.generated.dto.RoadmapDto;

import java.util.List;

public interface RoadmapService {
    RoadmapDto createRoadmap(RoadmapCreateDto roadmapCreateDto);
    RoadmapDto getRoadmapById(Long id);
    List<RoadmapDto> getRoadmaps(Long mentorId, Long menteeId);
    void deleteRoadmap(Long id);
}