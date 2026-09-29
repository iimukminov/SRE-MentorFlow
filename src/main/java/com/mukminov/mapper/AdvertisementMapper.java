package com.mukminov.mapper;

import com.mukminov.api.generated.dto.AdvertisementDto;
import com.mukminov.entity.Advertisement;
import org.springframework.stereotype.Component;

import java.time.ZoneOffset;

@Component
public class AdvertisementMapper {

    public AdvertisementDto toDto(Advertisement entity) {
        if (entity == null) {
            return null;
        }

        AdvertisementDto dto = new AdvertisementDto();
        dto.setId(entity.getId());
        dto.setUuid(entity.getUuid());
        dto.setAuthorId(entity.getAuthor().getId());
        dto.setAuthorUsername(entity.getAuthor().getUsername());
        dto.setType(entity.getType().name());
        dto.setTitle(entity.getTitle());
        dto.setContent(entity.getContent());
        dto.setStatus(entity.getStatus().name());

        if (entity.getCreatedAt() != null) {
            dto.setCreatedAt(entity.getCreatedAt().atOffset(ZoneOffset.UTC));
        }

        return dto;
    }
}