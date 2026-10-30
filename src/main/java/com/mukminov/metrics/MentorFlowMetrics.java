package com.mukminov.metrics;

import com.mukminov.repository.RoadmapRepository;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class MentorFlowMetrics {

    private final Counter usersRegisteredCounter;
    private final Counter authSuccessCounter;
    private final Counter authFailureCounter;
    private final Counter roadmapsCreatedCounter;
    private final Counter stepsCompletedCounter;
    private final Counter matchesCreatedCounter;
    private final Timer githubSyncTimer;

    public MentorFlowMetrics(MeterRegistry registry, RoadmapRepository roadmapRepository) {
        // 1. Количество зарегистрированных пользователей
        this.usersRegisteredCounter = Counter.builder("mentorflow_users_registered_total")
                .description("Total number of registered users in MentorFlow")
                .register(registry);

        // 2. Попытки авторизации (с разделением на успешные и неуспешные)
        this.authSuccessCounter = Counter.builder("mentorflow_auth_attempts_total")
                .tag("status", "success")
                .description("Total number of authentication attempts")
                .register(registry);

        this.authFailureCounter = Counter.builder("mentorflow_auth_attempts_total")
                .tag("status", "failure")
                .description("Total number of authentication attempts")
                .register(registry);

        // 3. Количество созданных роадмапов обучения
        this.roadmapsCreatedCounter = Counter.builder("mentorflow_roadmaps_created_total")
                .description("Total number of created mentorship roadmaps")
                .register(registry);

        // 4. Количество успешно завершённых шагов роадмапа (статус DONE)
        this.stepsCompletedCounter = Counter.builder("mentorflow_roadmap_steps_completed_total")
                .description("Total number of completed roadmap steps")
                .register(registry);

        // 5. Количество успешных мэтчей ментор-менти на доске объявлений
        this.matchesCreatedCounter = Counter.builder("mentorflow_matches_created_total")
                .description("Total number of accepted mentor-mentee connection requests")
                .register(registry);

        // 6. Текущее количество активных роадмапов в базе данных
        Gauge.builder("mentorflow_active_roadmaps_count", roadmapRepository, RoadmapRepository::count)
                .description("Current number of active roadmaps in database")
                .register(registry);

        // 7. Таймер длительности синхронизации коммитов с GitHub API
        this.githubSyncTimer = Timer.builder("mentorflow_github_sync_duration_seconds")
                .description("Duration of GitHub commits synchronization")
                .register(registry);
    }

    // Автоматический перехват успешных логинов через Spring Security
    @EventListener
    public void onAuthSuccess(AuthenticationSuccessEvent event) {
        authSuccessCounter.increment();
    }

    // Автоматический перехват неудачных логинов через Spring Security
    @EventListener
    public void onAuthFailure(AbstractAuthenticationFailureEvent event) {
        authFailureCounter.increment();
    }

    public void incrementUsersRegistered() {
        usersRegisteredCounter.increment();
    }

    public void incrementRoadmapsCreated() {
        roadmapsCreatedCounter.increment();
    }

    public void incrementStepsCompleted() {
        stepsCompletedCounter.increment();
    }

    public void incrementMatchesCreated() {
        matchesCreatedCounter.increment();
    }

    public void recordGitHubSyncDuration(long durationMs) {
        githubSyncTimer.record(durationMs, TimeUnit.MILLISECONDS);
    }
}