package com.mukminov.properties;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.time.DurationMin;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;

@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "app.github.client")
public class GitHubClientProperties {

    @NotNull
    @DurationMin(seconds = 1)
    private Duration connectTimeout = Duration.ofSeconds(10);

    @NotNull
    @DurationMin(seconds = 1)
    private Duration readTimeout = Duration.ofSeconds(10);

    @NotNull
    @DurationMin(seconds = 10)
    private Duration syncInterval = Duration.ofSeconds(60);

    @NotNull
    private String token;
}