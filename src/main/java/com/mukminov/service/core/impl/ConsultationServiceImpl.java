package com.mukminov.service.core.impl;

import com.mukminov.api.generated.dto.ConsultationCreateDto;
import com.mukminov.api.generated.dto.ConsultationDto;
import com.mukminov.api.generated.dto.ConsultationUpdateDto;
import com.mukminov.entity.Consultation;
import com.mukminov.entity.User;
import com.mukminov.mapper.ConsultationMapper;
import com.mukminov.repository.ConsultationRepository;
import com.mukminov.repository.UserRepository;
import com.mukminov.service.core.ConsultationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConsultationServiceImpl implements ConsultationService {

    private final ConsultationRepository consultationRepository;
    private final UserRepository userRepository;
    private final ConsultationMapper consultationMapper;

    @Override
    @Transactional
    public ConsultationDto createConsultation(ConsultationCreateDto createDto) {
        User mentor = userRepository.findById(createDto.getMentorId())
                .orElseThrow(() -> new RuntimeException("Mentor not found"));

        User mentee = userRepository.findById(createDto.getMenteeId())
                .orElseThrow(() -> new RuntimeException("Mentee not found"));

        Consultation consultation = Consultation.builder()
                .scheduledTime(createDto.getScheduledTime().toLocalDateTime())
                .status(Consultation.ConsultationStatus.SCHEDULED)
                .mentor(mentor)
                .mentee(mentee)
                .build();

        Consultation savedConsultation = consultationRepository.save(consultation);
        return consultationMapper.toDto(savedConsultation);
    }

    @Override
    @Transactional(readOnly = true)
    public ConsultationDto getConsultationById(Long id) {
        Consultation consultation = consultationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Consultation not found"));
        return consultationMapper.toDto(consultation);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConsultationDto> getConsultations(Long mentorId, Long menteeId) {
        List<Consultation> consultations;
        
        if (mentorId != null) {
            consultations = consultationRepository.findAllByMentorId(mentorId);
        } else if (menteeId != null) {
            consultations = consultationRepository.findAllByMenteeId(menteeId);
        } else {
            consultations = consultationRepository.findAll();
        }

        return consultations.stream()
                .map(consultationMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public ConsultationDto updateConsultation(Long id, ConsultationUpdateDto updateDto) {
        Consultation consultation = consultationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Consultation not found"));

        if (updateDto.getMeetLink() != null) {
            consultation.setMeetLink(updateDto.getMeetLink());
        }

        if (updateDto.getStatus() != null) {
            try {
                Consultation.ConsultationStatus newStatus = Consultation.ConsultationStatus.valueOf(updateDto.getStatus());
                consultation.setStatus(newStatus);
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Invalid status value: " + updateDto.getStatus());
            }
        }

        Consultation savedConsultation = consultationRepository.save(consultation);
        return consultationMapper.toDto(savedConsultation);
    }

    @Override
    @Transactional
    public void deleteConsultation(Long id) {
        consultationRepository.deleteById(id);
    }
}