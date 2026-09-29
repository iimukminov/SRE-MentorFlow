package com.mukminov.scheduler;

import com.mukminov.service.integration.GitHubSyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class GitHubSyncScheduler {

    private final GitHubSyncService gitHubSyncService;

    @Scheduled(fixedDelayString = "${app.github.client.sync-interval}")
    public void scheduleGitHubSync() {
        log.info("Starting scheduled GitHub sync...");
        try {
            gitHubSyncService.syncActiveSteps();
        } catch (Exception e) {
            log.error("Error occurred during scheduled GitHub sync", e);
        }
        log.info("Scheduled GitHub sync finished.");
    }
}