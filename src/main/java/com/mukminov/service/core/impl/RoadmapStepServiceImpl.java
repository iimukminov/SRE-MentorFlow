package com.mukminov.service.core.impl;

import com.mukminov.api.generated.dto.RoadmapStepCreateDto;
import com.mukminov.api.generated.dto.RoadmapStepDto;
import com.mukminov.api.generated.dto.RoadmapStepStatusUpdateDto;
import com.mukminov.entity.Roadmap;
import com.mukminov.entity.RoadmapStep;
import com.mukminov.mapper.RoadmapStepMapper;
import com.mukminov.repository.RoadmapRepository;
import com.mukminov.repository.RoadmapStepRepository;
import com.mukminov.service.core.RoadmapStepService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoadmapStepServiceImpl implements RoadmapStepService {

    private final RoadmapStepRepository stepRepository;
    private final RoadmapRepository roadmapRepository;
    private final RoadmapStepMapper stepMapper;

    @Override
    @Transactional
    public RoadmapStepDto addStepToRoadmap(Long roadmapId, RoadmapStepCreateDto createDto) {
        Roadmap roadmap = roadmapRepository.findById(roadmapId)
                .orElseThrow(() -> new RuntimeException("Roadmap not found"));

        RoadmapStep.StepStatus initialStatus = (createDto.getStepOrder() == 1)
                ? RoadmapStep.StepStatus.IN_PROGRESS
                : RoadmapStep.StepStatus.LOCKED;

        LocalDateTime startedAt = (initialStatus == RoadmapStep.StepStatus.IN_PROGRESS)
                ? LocalDateTime.now()
                : null;

        RoadmapStep step = RoadmapStep.builder()
                .stepOrder(createDto.getStepOrder())
                .title(createDto.getTitle())
                .contentLink(createDto.getContentLink())
                .requiredCommits(createDto.getRequiredCommits())
                .status(initialStatus)
                .startedAt(startedAt)
                .roadmap(roadmap)
                .build();

        RoadmapStep savedStep = stepRepository.save(step);
        return stepMapper.toDto(savedStep);
    }

    @Override
    @Transactional(readOnly = true)
    public RoadmapStepDto getStepById(Long id) {
        RoadmapStep step = stepRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Roadmap step not found"));
        return stepMapper.toDto(step);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoadmapStepDto> getStepsByRoadmapId(Long roadmapId) {
        return stepRepository.findAllByRoadmapIdOrderByStepOrderAsc(roadmapId).stream()
                .map(stepMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public RoadmapStepDto changeStepStatus(Long stepId, RoadmapStepStatusUpdateDto statusDto) {
        RoadmapStep step = stepRepository.findById(stepId)
                .orElseThrow(() -> new RuntimeException("Roadmap step not found"));

        try {
            RoadmapStep.StepStatus newStatus = RoadmapStep.StepStatus.valueOf(statusDto.getStatus());

            if (newStatus == RoadmapStep.StepStatus.IN_PROGRESS && step.getStatus() == RoadmapStep.StepStatus.IN_PROGRESS) {
                step.setStartedAt(LocalDateTime.now());
            }
            step.setStatus(newStatus);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid status value: " + statusDto.getStatus());
        }

        RoadmapStep savedStep = stepRepository.save(step);
        return stepMapper.toDto(savedStep);
    }

    @Override
    @Transactional
    public void deleteStep(Long id) {
        stepRepository.deleteById(id);
    }
}