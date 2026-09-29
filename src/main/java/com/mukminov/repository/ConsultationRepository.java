package com.mukminov.repository;

import com.mukminov.entity.Consultation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsultationRepository extends JpaRepository<Consultation, Long> {
    List<Consultation> findAllByMentorId(Long mentorId);
    List<Consultation> findAllByMenteeId(Long menteeId);
    boolean existsByMenteeIdAndStatus(Long menteeId, Consultation.ConsultationStatus status);
}