package com.mukminov.mapper;

import com.mukminov.api.generated.dto.ConsultationDto;
import com.mukminov.entity.Consultation;
import org.springframework.stereotype.Component;

import java.time.ZoneOffset;

@Component
public class ConsultationMapper {

    public ConsultationDto toDto(Consultation entity) {
        if (entity == null) {
            return null;
        }

        ConsultationDto dto = new ConsultationDto();
        dto.setId(entity.getId());
        
        if (entity.getUuid() != null) {
            dto.setUuid(entity.getUuid());
        }
        
        if (entity.getScheduledTime() != null) {
            dto.setScheduledTime(entity.getScheduledTime().atOffset(ZoneOffset.UTC));
        }
        
        dto.setMeetLink(entity.getMeetLink());
        
        if (entity.getStatus() != null) {
            dto.setStatus(entity.getStatus().name());
        }
        
        if (entity.getMentor() != null) {
            dto.setMentorId(entity.getMentor().getId());
            dto.setMentorUsername(entity.getMentor().getUsername());
        }
        
        if (entity.getMentee() != null) {
            dto.setMenteeId(entity.getMentee().getId());
            dto.setMenteeUsername(entity.getMentee().getUsername());
        }

        return dto;
    }
}