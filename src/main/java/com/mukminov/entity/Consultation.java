package com.mukminov.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "consultations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Consultation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @EqualsAndHashCode.Include
    @Column(updatable = false, nullable = false, unique = true)
    private UUID uuid = UUID.randomUUID();

    @Column(name = "scheduled_time", nullable = false)
    private LocalDateTime scheduledTime;

    @Column(name = "meet_link")
    private String meetLink;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ConsultationStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mentor_id", nullable = false)
    private User mentor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mentee_id", nullable = false)
    private User mentee;

    public enum ConsultationStatus {
        SCHEDULED, COMPLETED, CANCELLED
    }

    @Builder
    public Consultation(LocalDateTime scheduledTime, String meetLink, ConsultationStatus status, User mentor, User mentee) {
        this.scheduledTime = scheduledTime;
        this.meetLink = meetLink;
        this.status = status;
        this.mentor = mentor;
        this.mentee = mentee;
    }
}