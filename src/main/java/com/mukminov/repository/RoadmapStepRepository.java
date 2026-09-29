package com.mukminov.repository;

import com.mukminov.entity.RoadmapStep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoadmapStepRepository extends JpaRepository<RoadmapStep, Long> {
    List<RoadmapStep> findAllByRoadmapIdOrderByStepOrderAsc(Long roadmapId);
    List<RoadmapStep> findAllByStatus(RoadmapStep.StepStatus status);

    @Query("SELECT s FROM RoadmapStep s JOIN FETCH s.roadmap r JOIN FETCH r.mentee m WHERE s.status = :status")
    List<RoadmapStep> findAllByStatusWithMentee(@Param("status") RoadmapStep.StepStatus status);
    Optional<RoadmapStep> findByRoadmapIdAndStepOrder(Long roadmapId, Integer stepOrder);
}