package com.mukminov.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "review_feedbacks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ReviewFeedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @EqualsAndHashCode.Include
    @Column(updatable = false, nullable = false, unique = true)
    private UUID uuid = UUID.randomUUID();

    @Column(columnDefinition = "TEXT")
    private String comments;

    @Column(name = "is_approved", nullable = false)
    private Boolean isApproved;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roadmap_step_id", nullable = false)
    private RoadmapStep roadmapStep;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mentor_id", nullable = false)
    private User mentor;

    @Builder
    public ReviewFeedback(String comments, Boolean isApproved, RoadmapStep roadmapStep, User mentor) {
        this.comments = comments;
        this.isApproved = isApproved;
        this.roadmapStep = roadmapStep;
        this.mentor = mentor;
    }
}