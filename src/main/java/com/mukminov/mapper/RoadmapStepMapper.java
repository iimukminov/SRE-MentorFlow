package com.mukminov.mapper;

import com.mukminov.api.generated.dto.ReviewFeedbackDto;
import com.mukminov.api.generated.dto.RoadmapStepDto;
import com.mukminov.entity.RoadmapStep;
import org.springframework.stereotype.Component;

import java.time.ZoneOffset;

@Component
public class RoadmapStepMapper {

    public RoadmapStepDto toDto(RoadmapStep entity) {
        if (entity == null) {
            return null;
        }

        RoadmapStepDto dto = new RoadmapStepDto();
        dto.setId(entity.getId());
        
        if (entity.getUuid() != null) {
            dto.setUuid(entity.getUuid());
        }
        
        dto.setStepOrder(entity.getStepOrder());
        dto.setTitle(entity.getTitle());
        dto.setContentLink(entity.getContentLink());
        dto.setRequiredCommits(entity.getRequiredCommits());
        dto.setActualCommits(entity.getActualCommits() != null ? entity.getActualCommits() : 0);
        
        if (entity.getStatus() != null) {
            dto.setStatus(entity.getStatus().name());
        }
        
        if (entity.getRoadmap() != null) {
            dto.setRoadmapId(entity.getRoadmap().getId());
        }

        if (entity.getStartedAt() != null) {
            dto.setStartedAt(entity.getStartedAt().atOffset(ZoneOffset.UTC));
        }

        if (entity.getReviewFeedback() != null) {
            ReviewFeedbackDto feedbackDto = new ReviewFeedbackDto();
            feedbackDto.setComments(entity.getReviewFeedback().getComments());
            feedbackDto.setIsApproved(entity.getReviewFeedback().getIsApproved());
            dto.setReviewFeedback(feedbackDto);
        }


        return dto;
    }
}