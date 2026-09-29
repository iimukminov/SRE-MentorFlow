package com.mukminov.service.integration.impl;

import com.mukminov.api.generated.dto.RoadmapStepDto;
import com.mukminov.client.GitHubClient;
import com.mukminov.entity.Consultation;
import com.mukminov.entity.RoadmapStep;
import com.mukminov.mapper.RoadmapStepMapper;
import com.mukminov.repository.ConsultationRepository;
import com.mukminov.repository.RoadmapStepRepository;
import com.mukminov.service.integration.GitHubSyncService;
import com.mukminov.util.GitHubUrlParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GitHubSyncServiceImpl implements GitHubSyncService {

    private final RoadmapStepRepository stepRepository;
    private final ConsultationRepository consultationRepository;
    private final RoadmapStepMapper stepMapper;
    private final GitHubClient gitHubClient;

    @Override
    public void syncActiveSteps() {
        List<RoadmapStep> activeSteps = stepRepository.findAllByStatusWithMentee(RoadmapStep.StepStatus.IN_PROGRESS);
        
        if (activeSteps.isEmpty()) {
            log.debug("No active roadmap steps found for GitHub sync.");
            return;
        }

        log.info("Processing {} active steps for GitHub sync.", activeSteps.size());

        for (RoadmapStep step : activeSteps) {
            processStep(step);
        }
    }

    @Override
    public RoadmapStepDto syncSingleStep(Long stepId) {
        RoadmapStep step = stepRepository.findById(stepId)
                .orElseThrow(() -> new RuntimeException("Шаг не найден"));

        if (step.getStatus() == RoadmapStep.StepStatus.IN_PROGRESS) {
            processStep(step);
        }
        return stepMapper.toDto(step);
    }

    private void processStep(RoadmapStep step) {
        String repoUrl = step.getContentLink();
        String githubUsername = step.getRoadmap().getMentee().getGithubUsername();

        if (githubUsername == null || githubUsername.isEmpty() || step.getStartedAt() == null) {
            return;
        }

        String[] ownerAndRepo = GitHubUrlParser.extractOwnerAndRepo(repoUrl);
        if (ownerAndRepo == null) return;

        String owner = ownerAndRepo[0];
        String repo = ownerAndRepo[1];
        String sinceIso = step.getStartedAt().atOffset(ZoneOffset.UTC)
                .format(DateTimeFormatter.ISO_INSTANT);

        int actualCommits = gitHubClient.getCommitsCount(owner, repo, githubUsername, sinceIso);

        log.info("Step ID: {} | Required: {} | Found: {}", step.getId(), step.getRequiredCommits(), actualCommits);

        step.setActualCommits(actualCommits);

        if (actualCommits >= step.getRequiredCommits()) {
            log.info("Step ID: {} completed required commits. Moving to REVIEW.", step.getId());
            step.setStatus(RoadmapStep.StepStatus.REVIEW);
        } else {
            int maxAfkDays = step.getRoadmap().getMaxAfkDays() != null ? step.getRoadmap().getMaxAfkDays() : 5;
            String meetLink = step.getRoadmap().getMeetLink() != null ? step.getRoadmap().getMeetLink() : "https://meet.google.com/new";

            if (step.getStartedAt().plusDays(maxAfkDays).isBefore(LocalDateTime.now())) {
                Long menteeId = step.getRoadmap().getMentee().getId();

                boolean hasActiveConsultation = consultationRepository.existsByMenteeIdAndStatus(
                        menteeId, Consultation.ConsultationStatus.SCHEDULED);

                if (!hasActiveConsultation) {
                    log.warn("Mentee {} is slacking ({} days). Auto-scheduling intervention!", githubUsername, maxAfkDays);

                    Consultation intervention = Consultation.builder()
                            .mentor(step.getRoadmap().getMentor())
                            .mentee(step.getRoadmap().getMentee())
                            .scheduledTime(LocalDateTime.now().plusDays(1))
                            .meetLink(meetLink)
                            .status(Consultation.ConsultationStatus.SCHEDULED)
                            .build();

                    consultationRepository.save(intervention);
                }
            }
        }

        stepRepository.save(step);
    }
}