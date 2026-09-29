package com.mukminov.controller.api;

import com.mukminov.api.generated.api.RoadmapsApi;
import com.mukminov.api.generated.dto.RoadmapCreateDto;
import com.mukminov.api.generated.dto.RoadmapDto;
import com.mukminov.service.core.RoadmapService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RoadmapController implements RoadmapsApi {

    private final RoadmapService roadmapService;

    @Override
    public ResponseEntity<RoadmapDto> createRoadmap(RoadmapCreateDto roadmapCreateDto) {
        RoadmapDto created = roadmapService.createRoadmap(roadmapCreateDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Override
    public ResponseEntity<Void> deleteRoadmap(Long id) {
        roadmapService.deleteRoadmap(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<RoadmapDto> getRoadmapById(Long id) {
        return ResponseEntity.ok(roadmapService.getRoadmapById(id));
    }

    @Override
    public ResponseEntity<List<RoadmapDto>> getRoadmaps(Long mentorId, Long menteeId) {
        return ResponseEntity.ok(roadmapService.getRoadmaps(mentorId, menteeId));
    }
}
