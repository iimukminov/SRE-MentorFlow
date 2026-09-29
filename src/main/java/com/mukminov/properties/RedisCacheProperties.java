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
@ConfigurationProperties(prefix = "app.redis.cache")
public class RedisCacheProperties {

    @NotNull
    @DurationMin(seconds = 1)
    private Duration ttlMinutes = Duration.ofMinutes(5);
}
