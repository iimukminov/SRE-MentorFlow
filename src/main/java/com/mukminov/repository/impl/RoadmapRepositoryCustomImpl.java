package com.mukminov.repository.impl;

import com.mukminov.entity.Roadmap;
import com.mukminov.repository.RoadmapRepositoryCustom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class RoadmapRepositoryCustomImpl implements RoadmapRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Roadmap> searchRoadmaps(String title, Long mentorId, Long menteeId) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Roadmap> query = criteriaBuilder.createQuery(Roadmap.class);
        Root<Roadmap> root = query.from(Roadmap.class);

        root.fetch("mentor", JoinType.INNER);
        root.fetch("mentee", JoinType.INNER);

        List<Predicate> predicates = new ArrayList<>();

        if (title != null && !title.isBlank()) {
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), "%" + title.toLowerCase() + "%"));
        }
        
        if (mentorId != null) {
            predicates.add(criteriaBuilder.equal(root.get("mentor").get("id"), mentorId));
        }
        
        if (menteeId != null) {
            predicates.add(criteriaBuilder.equal(root.get("mentee").get("id"), menteeId));
        }

        query.where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));

        query.orderBy(criteriaBuilder.desc(root.get("createdAt")));

        return entityManager.createQuery(query).getResultList();
    }
}