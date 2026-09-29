package com.mukminov.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "roadmaps")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Roadmap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @EqualsAndHashCode.Include
    @Column(updatable = false, nullable = false, unique = true)
    private UUID uuid = UUID.randomUUID();

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mentor_id", nullable = false)
    private User mentor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mentee_id", nullable = false)
    private User mentee;

    @OneToMany(mappedBy = "roadmap", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RoadmapStep> steps = new ArrayList<>();

    @Column(name = "max_afk_days")
    private Integer maxAfkDays;

    @Column(name = "meet_link")
    private String meetLink;

    @Builder
    public Roadmap(String title, String description, User mentor, User mentee, Integer maxAfkDays, String meetLink) {
        this.title = title;
        this.description = description;
        this.mentor = mentor;
        this.mentee = mentee;
        this.maxAfkDays = maxAfkDays;
        this.meetLink = meetLink;
    }
}