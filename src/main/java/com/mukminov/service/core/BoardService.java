package com.mukminov.service.core;

import com.mukminov.api.generated.dto.*;
import java.util.List;

public interface BoardService {
    List<AdvertisementDto> getAdvertisements(String type);
    AdvertisementDto createAdvertisement(AdvertisementCreateDto dto);
    ConnectionRequestDto replyToAdvertisement(Long adId, ConnectionRequestCreateDto dto);
    List<ConnectionRequestDto> getIncomingRequests(Long userId);
    ConnectionRequestDto updateRequestStatus(Long requestId, ConnectionRequestStatusUpdateDto dto);
}