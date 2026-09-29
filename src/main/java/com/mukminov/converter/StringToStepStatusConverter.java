package com.mukminov.converter;

import com.mukminov.entity.RoadmapStep;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToStepStatusConverter implements Converter<String, RoadmapStep.StepStatus> {

    @Override
    public RoadmapStep.StepStatus convert(String source) {
        if (source == null || source.isBlank()) {
            return null;
        }
        try {
            return RoadmapStep.StepStatus.valueOf(source.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unknown RoadmapStep status: " + source);
        }
    }
}