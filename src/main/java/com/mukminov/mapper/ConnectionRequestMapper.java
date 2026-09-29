package com.mukminov.mapper;

import com.mukminov.api.generated.dto.ConnectionRequestDto;
import com.mukminov.entity.ConnectionRequest;
import org.springframework.stereotype.Component;

import java.time.ZoneOffset;

@Component
public class ConnectionRequestMapper {

    public ConnectionRequestDto toDto(ConnectionRequest entity) {
        if (entity == null) {
            return null;
        }

        ConnectionRequestDto dto = new ConnectionRequestDto();
        dto.setId(entity.getId());
        dto.setUuid(entity.getUuid());
        dto.setAdvertisementId(entity.getAdvertisement().getId());
        dto.setAdvertisementTitle(entity.getAdvertisement().getTitle());
        dto.setSenderId(entity.getSender().getId());
        dto.setSenderUsername(entity.getSender().getUsername());
        dto.setCoverLetter(entity.getCoverLetter());
        dto.setStatus(entity.getStatus().name());

        if (entity.getCreatedAt() != null) {
            dto.setCreatedAt(entity.getCreatedAt().atOffset(ZoneOffset.UTC));
        }

        return dto;
    }
}