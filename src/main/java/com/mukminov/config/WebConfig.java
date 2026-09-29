package com.mukminov.config;

import com.mukminov.converter.StringToStepStatusConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final StringToStepStatusConverter stringToStepStatusConverter;

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(stringToStepStatusConverter);
    }
}