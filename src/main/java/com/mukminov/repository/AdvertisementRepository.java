package com.mukminov.repository;

import com.mukminov.entity.Advertisement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdvertisementRepository extends JpaRepository<Advertisement, Long> {

    @Query("SELECT a FROM Advertisement a JOIN FETCH a.author WHERE a.status = :status ORDER BY a.createdAt DESC")
    List<Advertisement> findAllByStatusOrderByCreatedAtDesc(@Param("status") Advertisement.AdStatus status);

    @Query("SELECT a FROM Advertisement a JOIN FETCH a.author WHERE a.type = :type AND a.status = :status ORDER BY a.createdAt DESC")
    List<Advertisement> findAllByTypeAndStatusOrderByCreatedAtDesc(@Param("type") Advertisement.AdType type, @Param("status") Advertisement.AdStatus status);
}