package com.mukminov.controller.api;

import com.mukminov.api.generated.api.ReviewFeedbackApi;
import com.mukminov.api.generated.dto.ReviewFeedbackCreateDto;
import com.mukminov.api.generated.dto.ReviewFeedbackDto;
import com.mukminov.service.core.ReviewFeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReviewFeedbackController implements ReviewFeedbackApi {

    private final ReviewFeedbackService reviewFeedbackService;

    @Override
    public ResponseEntity<ReviewFeedbackDto> addFeedbackToStep(Long stepId, ReviewFeedbackCreateDto reviewFeedbackCreateDto) {
        ReviewFeedbackDto created = reviewFeedbackService.addFeedbackToStep(stepId, reviewFeedbackCreateDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Override
    public ResponseEntity<ReviewFeedbackDto> getFeedbackByStepId(Long stepId) {
        return ResponseEntity.ok(reviewFeedbackService.getFeedbackByStepId(stepId));
    }
}