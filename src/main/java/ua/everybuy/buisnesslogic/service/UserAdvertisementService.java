package ua.everybuy.buisnesslogic.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ua.everybuy.database.entity.Advertisement;
import ua.everybuy.routing.dto.AdvertisementDto;
import ua.everybuy.routing.dto.mapper.AdvertisementMapper;
import ua.everybuy.routing.dto.response.AdvertisementWithStatisticResponse;
import ua.everybuy.routing.dto.response.StatusResponse;

import java.security.Principal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserAdvertisementService {
    private final AdvertisementService advertisementService;
    private final AdvertisementMapper advertisementMapper;

    public StatusResponse<AdvertisementDto> getUserAdvertisement(Long id, HttpServletRequest request, Principal principal) {

        Long userId = Long.parseLong(principal.getName());
        Advertisement advertisement = advertisementService.findAdvertisementByIdAndUserId(id, userId);
        AdvertisementDto advertisementDTO = advertisementService.createAdvertisementDto(advertisement, userId, request);

        return new StatusResponse<>(HttpStatus.OK.value(), advertisementDTO);
    }

    public List<AdvertisementWithStatisticResponse> getUserAdvertisementsByEnabledStatus(Long userId, boolean isEnabled) {
        return advertisementService.findAllUserAdvertisement(userId)
                .stream()
                .filter(ad -> ad.getIsEnabled() == isEnabled)
                .map(advertisementMapper::mapToAdvertisementStatisticResponse)
                .toList();
    }
}
