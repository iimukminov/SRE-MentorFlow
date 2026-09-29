package com.mukminov.repository;

import com.mukminov.entity.ConnectionRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConnectionRequestRepository extends JpaRepository<ConnectionRequest, Long> {

    @Query("SELECT r FROM ConnectionRequest r JOIN FETCH r.advertisement JOIN FETCH r.sender WHERE r.advertisement.author.id = :authorId ORDER BY r.createdAt DESC")
    List<ConnectionRequest> findAllByAdvertisementAuthorIdOrderByCreatedAtDesc(@Param("authorId") Long authorId);

    boolean existsBySenderIdAndAdvertisementId(Long senderId, Long advertisementId);
}