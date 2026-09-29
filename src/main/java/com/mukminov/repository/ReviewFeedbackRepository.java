package com.mukminov.repository;

import com.mukminov.entity.ReviewFeedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewFeedbackRepository extends JpaRepository<ReviewFeedback, Long> {
    Optional<ReviewFeedback> findByRoadmapStepId(Long roadmapStepId);
}