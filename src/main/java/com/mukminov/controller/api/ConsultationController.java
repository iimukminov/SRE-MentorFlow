package com.mukminov.controller.api;

import com.mukminov.api.generated.api.ConsultationsApi;
import com.mukminov.api.generated.dto.ConsultationCreateDto;
import com.mukminov.api.generated.dto.ConsultationDto;
import com.mukminov.api.generated.dto.ConsultationUpdateDto;
import com.mukminov.service.core.ConsultationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ConsultationController implements ConsultationsApi {

    private final ConsultationService consultationService;

    @Override
    public ResponseEntity<ConsultationDto> createConsultation(ConsultationCreateDto consultationCreateDto) {
        ConsultationDto created = consultationService.createConsultation(consultationCreateDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Override
    public ResponseEntity<ConsultationDto> getConsultationById(Long id) {
        return ResponseEntity.ok(consultationService.getConsultationById(id));
    }

    @Override
    public ResponseEntity<List<ConsultationDto>> getConsultations(Long mentorId, Long menteeId) {
        return ResponseEntity.ok(consultationService.getConsultations(mentorId, menteeId));
    }

    @Override
    public ResponseEntity<ConsultationDto> updateConsultation(Long id, ConsultationUpdateDto consultationUpdateDto) {
        return ResponseEntity.ok(consultationService.updateConsultation(id, consultationUpdateDto));
    }

    @Override
    public ResponseEntity<Void> deleteConsultation(Long id) {
        consultationService.deleteConsultation(id);
        return ResponseEntity.noContent().build();
    }
}