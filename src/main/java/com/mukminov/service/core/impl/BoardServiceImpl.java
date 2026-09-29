package com.mukminov.service.core.impl;

import com.mukminov.api.generated.dto.*;
import com.mukminov.entity.*;
import com.mukminov.mapper.*;
import com.mukminov.repository.*;
import com.mukminov.service.core.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final AdvertisementRepository adRepository;
    private final ConnectionRequestRepository requestRepository;
    private final UserRepository userRepository;
    private final AdvertisementMapper adMapper;
    private final ConnectionRequestMapper reqMapper;

    @Override
    @Transactional(readOnly = true)
    public List<AdvertisementDto> getAdvertisements(String type) {
        List<Advertisement> ads;
        if (type != null && !type.isBlank()) {
            ads = adRepository.findAllByTypeAndStatusOrderByCreatedAtDesc(
                    Advertisement.AdType.valueOf(type), Advertisement.AdStatus.ACTIVE);
        } else {
            ads = adRepository.findAllByStatusOrderByCreatedAtDesc(Advertisement.AdStatus.ACTIVE);
        }
        return ads.stream().map(adMapper::toDto).toList();
    }

    @Override
    @Transactional
    public AdvertisementDto createAdvertisement(AdvertisementCreateDto dto) {
        User author = userRepository.findById(dto.getAuthorId())
                .orElseThrow(() -> new RuntimeException("Author not found"));

        Advertisement ad = Advertisement.builder()
                .author(author)
                .type(Advertisement.AdType.valueOf(dto.getType()))
                .title(dto.getTitle())
                .content(dto.getContent())
                .status(Advertisement.AdStatus.ACTIVE)
                .build();

        return adMapper.toDto(adRepository.save(ad));
    }

    @Override
    @Transactional
    public ConnectionRequestDto replyToAdvertisement(Long adId, ConnectionRequestCreateDto dto) {
        Advertisement ad = adRepository.findById(adId)
                .orElseThrow(() -> new RuntimeException("Advertisement not found"));
        User sender = userRepository.findById(dto.getSenderId())
                .orElseThrow(() -> new RuntimeException("Sender not found"));

        boolean alreadySent = requestRepository.existsBySenderIdAndAdvertisementId(dto.getSenderId(), adId);
        if (alreadySent) {
            throw new RuntimeException("Вы уже отправляли отклик на эту заявку!");
        }

        ConnectionRequest req = ConnectionRequest.builder()
                .advertisement(ad)
                .sender(sender)
                .coverLetter(dto.getCoverLetter())
                .status(ConnectionRequest.RequestStatus.PENDING)
                .build();

        return reqMapper.toDto(requestRepository.save(req));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConnectionRequestDto> getIncomingRequests(Long userId) {
        return requestRepository.findAllByAdvertisementAuthorIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(reqMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public ConnectionRequestDto updateRequestStatus(Long requestId, ConnectionRequestStatusUpdateDto dto) {
        ConnectionRequest req = requestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));
        
        ConnectionRequest.RequestStatus newStatus = ConnectionRequest.RequestStatus.valueOf(dto.getStatus());
        req.setStatus(newStatus);

        if (newStatus == ConnectionRequest.RequestStatus.ACCEPTED) {
            Advertisement ad = req.getAdvertisement();
            Long mentorId;
            Long menteeId;

            if (ad.getType() == Advertisement.AdType.SEEKING_MENTOR) {
                menteeId = ad.getAuthor().getId();
                mentorId = req.getSender().getId();
            } else {
                mentorId = ad.getAuthor().getId();
                menteeId = req.getSender().getId();
            }

            userRepository.addConnection(mentorId, menteeId);
            log.info("Match created! Mentor ID: {}, Mentee ID: {}", mentorId, menteeId);

            ad.setStatus(Advertisement.AdStatus.CLOSED);
            adRepository.save(ad);
        }

        return reqMapper.toDto(requestRepository.save(req));
    }
}