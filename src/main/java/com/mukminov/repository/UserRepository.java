package com.mukminov.repository;

import com.mukminov.api.generated.dto.UserDto;
import com.mukminov.entity.RoadmapStep;
import com.mukminov.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    @Query(value = "SELECT u.* FROM users u JOIN user_connections uc ON u.id = uc.mentee_id WHERE uc.mentor_id = :mentorId", nativeQuery = true)
    List<User> findConnectedMentees(@Param("mentorId") Long mentorId);

    @Modifying
    @Query(value = "INSERT INTO user_connections (mentor_id, mentee_id) VALUES (:mentorId, :menteeId) ON CONFLICT DO NOTHING", nativeQuery = true)
    void addConnection(@Param("mentorId") Long mentorId, @Param("menteeId") Long menteeId);

    @Query("""
             SELECT u FROM User u WHERE u.id IN (
             SELECT cr.sender.id FROM ConnectionRequest cr
             WHERE cr.advertisement.author.id = :mentorId 
             AND cr.status = 'ACCEPTED')
            """)
    List<User> findAllMenteesByMentorId(@Param("mentorId") Long mentorId);
}