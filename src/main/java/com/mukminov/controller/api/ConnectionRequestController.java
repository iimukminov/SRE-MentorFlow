package com.mukminov.controller.api;

import com.mukminov.api.generated.api.ConnectionsApi;
import com.mukminov.api.generated.dto.ConnectionRequestDto;
import com.mukminov.api.generated.dto.ConnectionRequestStatusUpdateDto;
import com.mukminov.service.core.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ConnectionRequestController implements ConnectionsApi {

    private final BoardService boardService;

    @Override
    public ResponseEntity<List<ConnectionRequestDto>> getIncomingRequests(Long userId) {
        return ResponseEntity.ok(boardService.getIncomingRequests(userId));
    }

    @Override
    public ResponseEntity<ConnectionRequestDto> updateRequestStatus(Long id, ConnectionRequestStatusUpdateDto connectionRequestStatusUpdateDto) {
        return ResponseEntity.ok(boardService.updateRequestStatus(id, connectionRequestStatusUpdateDto));
    }
}