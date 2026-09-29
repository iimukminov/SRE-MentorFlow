package com.mukminov.mapper;

import com.mukminov.api.generated.dto.ReviewFeedbackDto;
import com.mukminov.entity.ReviewFeedback;
import org.springframework.stereotype.Component;

import java.time.ZoneOffset;

@Component
public class ReviewFeedbackMapper {

    public ReviewFeedbackDto toDto(ReviewFeedback entity) {
        if (entity == null) {
            return null;
        }

        ReviewFeedbackDto dto = new ReviewFeedbackDto();
        dto.setId(entity.getId());
        
        if (entity.getUuid() != null) {
            dto.setUuid(entity.getUuid());
        }
        
        dto.setComments(entity.getComments());
        dto.setIsApproved(entity.getIsApproved());
        
        if (entity.getCreatedAt() != null) {
            dto.setCreatedAt(entity.getCreatedAt().atOffset(ZoneOffset.UTC));
        }
        
        if (entity.getRoadmapStep() != null) {
            dto.setRoadmapStepId(entity.getRoadmapStep().getId());
        }
        
        if (entity.getMentor() != null) {
            dto.setMentorId(entity.getMentor().getId());
            dto.setMentorUsername(entity.getMentor().getUsername());
        }

        return dto;
    }
}