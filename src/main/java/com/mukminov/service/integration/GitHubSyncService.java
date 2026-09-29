package com.mukminov.service.integration;

import com.mukminov.api.generated.dto.RoadmapStepDto;

public interface GitHubSyncService {
    void syncActiveSteps();
    RoadmapStepDto syncSingleStep(Long stepId);
}