package com.mukminov.service.core;

import com.mukminov.api.generated.dto.RoadmapStepCreateDto;
import com.mukminov.api.generated.dto.RoadmapStepDto;
import com.mukminov.api.generated.dto.RoadmapStepStatusUpdateDto;

import java.util.List;

public interface RoadmapStepService {
    RoadmapStepDto addStepToRoadmap(Long roadmapId, RoadmapStepCreateDto createDto);
    RoadmapStepDto getStepById(Long id);
    List<RoadmapStepDto> getStepsByRoadmapId(Long roadmapId);
    RoadmapStepDto changeStepStatus(Long stepId, RoadmapStepStatusUpdateDto statusDto);
    void deleteStep(Long id);
}