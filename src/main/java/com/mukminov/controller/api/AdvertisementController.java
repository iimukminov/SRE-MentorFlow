package com.mukminov.controller.api;

import com.mukminov.api.generated.api.BoardApi;
import com.mukminov.api.generated.dto.AdvertisementCreateDto;
import com.mukminov.api.generated.dto.AdvertisementDto;
import com.mukminov.api.generated.dto.ConnectionRequestCreateDto;
import com.mukminov.api.generated.dto.ConnectionRequestDto;
import com.mukminov.service.core.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AdvertisementController implements BoardApi {

    private final BoardService boardService;

    @Override
    public ResponseEntity<List<AdvertisementDto>> getAdvertisements(String type) {
        return ResponseEntity.ok(boardService.getAdvertisements(type));
    }

    @Override
    public ResponseEntity<AdvertisementDto> createAdvertisement(AdvertisementCreateDto advertisementCreateDto) {
        AdvertisementDto created = boardService.createAdvertisement(advertisementCreateDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Override
    public ResponseEntity<ConnectionRequestDto> replyToAdvertisement(Long id, ConnectionRequestCreateDto connectionRequestCreateDto) {
        ConnectionRequestDto reply = boardService.replyToAdvertisement(id, connectionRequestCreateDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(reply);
    }
}