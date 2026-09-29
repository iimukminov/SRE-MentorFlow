package com.mukminov.repository;

import com.mukminov.entity.Roadmap;
import java.util.List;

public interface RoadmapRepositoryCustom {
    List<Roadmap> searchRoadmaps(String title, Long mentorId, Long menteeId);
}