package com.mukminov.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mukminov.properties.GitHubClientProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class GitHubClient {

    private final OkHttpClient okHttpClient;
    private final ObjectMapper objectMapper;
    private final GitHubClientProperties properties;

    @Cacheable(value = "githubCommits", key = "#owner + ':' + #repo + ':' + #githubUsername")
    public int getCommitsCount(String owner, String repo, String githubUsername, String sinceIso) {
        String url = String.format("https://api.github.com/repos/%s/%s/commits?since=%s&author=%s",
                owner, repo, sinceIso, githubUsername);

        log.debug("Sending request to GitHub API: {}", url);

        Request request = new Request.Builder()
                .url(url)
                .header("Accept", "application/vnd.github+json")
                .header("User-Agent", "MentorFlow-App")
                .header("Authorization", "Bearer " + properties.getToken())
                .build();

        try (Response response = okHttpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                log.warn("GitHub API returned status {} for repository {}/{}", response.code(), owner, repo);
                return 0;
            }

            String responseBody = response.body() != null ? response.body().string() : "[]";
            JsonNode rootArray = objectMapper.readTree(responseBody);

            if (rootArray.isArray()) {
                int count = rootArray.size();
                log.debug("Found {} commits for user {} since {}", count, githubUsername, sinceIso);
                return count;
            }
        } catch (IOException e) {
            log.error("Network error while calling GitHub API for {}/{}", owner, repo, e);
        }

        return 0;
    }
}