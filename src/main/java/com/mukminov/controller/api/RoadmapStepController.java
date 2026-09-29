package com.mukminov.controller.api;

import com.mukminov.api.generated.api.RoadmapStepsApi;
import com.mukminov.api.generated.dto.RoadmapStepCreateDto;
import com.mukminov.api.generated.dto.RoadmapStepDto;
import com.mukminov.api.generated.dto.RoadmapStepStatusUpdateDto;
import com.mukminov.service.core.RoadmapStepService;
import com.mukminov.service.integration.GitHubSyncService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RoadmapStepController implements RoadmapStepsApi {

    private final RoadmapStepService roadmapStepService;
    private final GitHubSyncService  gitHubSyncService;

    @Override
    public ResponseEntity<RoadmapStepDto> addStepToRoadmap(Long roadmapId, RoadmapStepCreateDto roadmapStepCreateDto) {
        RoadmapStepDto createdStep = roadmapStepService.addStepToRoadmap(roadmapId, roadmapStepCreateDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdStep);
    }

    @Override
    public ResponseEntity<RoadmapStepDto> getStepById(Long id) {
        return ResponseEntity.ok(roadmapStepService.getStepById(id));
    }

    @Override
    public ResponseEntity<List<RoadmapStepDto>> getRoadmapSteps(Long roadmapId) {
        return ResponseEntity.ok(roadmapStepService.getStepsByRoadmapId(roadmapId));
    }

    @Override
    public ResponseEntity<RoadmapStepDto> changeStepStatus(Long id, RoadmapStepStatusUpdateDto roadmapStepStatusUpdateDto) {
        return ResponseEntity.ok(roadmapStepService.changeStepStatus(id, roadmapStepStatusUpdateDto));
    }

    @Override
    public ResponseEntity<Void> deleteStep(Long id) {
        roadmapStepService.deleteStep(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<RoadmapStepDto> syncStep(Long id) {
        RoadmapStepDto step = gitHubSyncService.syncSingleStep(id);
        return ResponseEntity.ok(roadmapStepService.getStepById(step.getId()));
    }
}