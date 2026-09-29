package com.mukminov.service.core;

import com.mukminov.api.generated.dto.ConsultationCreateDto;
import com.mukminov.api.generated.dto.ConsultationDto;
import com.mukminov.api.generated.dto.ConsultationUpdateDto;

import java.util.List;

public interface ConsultationService {
    ConsultationDto createConsultation(ConsultationCreateDto createDto);
    ConsultationDto getConsultationById(Long id);
    List<ConsultationDto> getConsultations(Long mentorId, Long menteeId);
    ConsultationDto updateConsultation(Long id, ConsultationUpdateDto updateDto);
    void deleteConsultation(Long id);
}