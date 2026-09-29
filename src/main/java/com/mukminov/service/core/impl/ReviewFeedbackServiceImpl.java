package com.mukminov.service.core.impl;

import com.mukminov.api.generated.dto.ReviewFeedbackCreateDto;
import com.mukminov.api.generated.dto.ReviewFeedbackDto;
import com.mukminov.entity.ReviewFeedback;
import com.mukminov.entity.RoadmapStep;
import com.mukminov.entity.User;
import com.mukminov.mapper.ReviewFeedbackMapper;
import com.mukminov.repository.ReviewFeedbackRepository;
import com.mukminov.repository.RoadmapStepRepository;
import com.mukminov.repository.UserRepository;
import com.mukminov.service.core.ReviewFeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewFeedbackServiceImpl implements ReviewFeedbackService {

    private final ReviewFeedbackRepository feedbackRepository;
    private final RoadmapStepRepository stepRepository;
    private final UserRepository userRepository;
    private final ReviewFeedbackMapper feedbackMapper;

    @Override
    @Transactional
    public ReviewFeedbackDto addFeedbackToStep(Long stepId, ReviewFeedbackCreateDto createDto) {
        RoadmapStep step = stepRepository.findById(stepId)
                .orElseThrow(() -> new RuntimeException("Roadmap step not found"));

        User mentor = userRepository.findById(createDto.getMentorId())
                .orElseThrow(() -> new RuntimeException("Mentor not found"));

        ReviewFeedback feedback = feedbackRepository.findByRoadmapStepId(stepId)
                .orElseGet(ReviewFeedback::new);

        feedback.setComments(createDto.getComments());
        feedback.setIsApproved(createDto.getIsApproved());
        feedback.setRoadmapStep(step);
        feedback.setMentor(mentor);

        ReviewFeedback savedFeedback = feedbackRepository.save(feedback);

        if (Boolean.TRUE.equals(createDto.getIsApproved())) {
            step.setStatus(RoadmapStep.StepStatus.DONE);
            stepRepository.save(step);

            stepRepository.findByRoadmapIdAndStepOrder(step.getRoadmap().getId(), step.getStepOrder() + 1)
                    .ifPresent(nextStep -> {
                        nextStep.setStatus(RoadmapStep.StepStatus.IN_PROGRESS);
                        nextStep.setStartedAt(java.time.LocalDateTime.now());
                        stepRepository.save(nextStep);
                    });
        } else {
            step.setStatus(RoadmapStep.StepStatus.IN_PROGRESS);
            stepRepository.save(step);
        }

        return feedbackMapper.toDto(savedFeedback);
    }

    @Override
    @Transactional(readOnly = true)
    public ReviewFeedbackDto getFeedbackByStepId(Long stepId) {
        ReviewFeedback feedback = feedbackRepository.findByRoadmapStepId(stepId)
                .orElseThrow(() -> new RuntimeException("Feedback not found for this step"));
        return feedbackMapper.toDto(feedback);
    }
}