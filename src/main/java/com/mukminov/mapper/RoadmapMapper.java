package com.mukminov.mapper;

import com.mukminov.api.generated.dto.RoadmapDto;
import com.mukminov.api.generated.dto.RoadmapStepDto;
import com.mukminov.entity.Roadmap;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.ZoneOffset;
import java.util.Comparator;

@Component
@RequiredArgsConstructor
public class RoadmapMapper {

    private final RoadmapStepMapper stepMapper;

    public RoadmapDto toDto(Roadmap entity) {
        if (entity == null) {
            return null;
        }

        RoadmapDto dto = new RoadmapDto();
        dto.setId(entity.getId());
        dto.setUuid(entity.getUuid());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        
        if (entity.getCreatedAt() != null) {
            dto.setCreatedAt(entity.getCreatedAt().atOffset(ZoneOffset.UTC));
        }
        
        if (entity.getMentor() != null) {
            dto.setMentorId(entity.getMentor().getId());
            dto.setMentorUsername(entity.getMentor().getUsername());
        }
        
        if (entity.getMentee() != null) {
            dto.setMenteeId(entity.getMentee().getId());
            dto.setMenteeUsername(entity.getMentee().getUsername());
        }

        dto.setMaxAfkDays(entity.getMaxAfkDays());
        dto.setMeetLink(entity.getMeetLink());

        if (entity.getSteps() != null) {
            dto.setSteps(entity.getSteps().stream()
                    .map(stepMapper::toDto)
                    .filter(step -> step != null && step.getStepOrder() != null)
                    .sorted(Comparator.comparingInt(RoadmapStepDto::getStepOrder))
                    .toList());
        }

        return dto;
    }
}