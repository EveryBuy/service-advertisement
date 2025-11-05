package ua.everybuy.routing.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ua.everybuy.service.advertisement.AdvertisementCreationService;
import ua.everybuy.service.advertisement.AdvertisementManagementService;
import ua.everybuy.service.advertisement.AdvertisementUpdateService;
import ua.everybuy.routing.dto.AdvertisementDto;
import ua.everybuy.routing.dto.request.UpdateAdvertisementRequest;
import ua.everybuy.routing.dto.response.*;
import ua.everybuy.routing.dto.request.CreateAdvertisementRequest;
import java.io.IOException;
import java.security.Principal;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class AdvertisementController {
    private final AdvertisementManagementService advertisementManagementService;
    private final AdvertisementCreationService advertisementCreationService;
    private final AdvertisementUpdateService advertisementUpdateService;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public StatusResponse<CreateAdvertisementResponse> createAdvertisement(
            @Valid @RequestPart("request") CreateAdvertisementRequest createRequest,
            @Nullable @RequestPart(value = "photos", required = false) MultipartFile[] photos,
            Principal principal) throws IOException {
        return advertisementCreationService.createAdvertisement(createRequest, photos, principal.getName());
    }

    @PutMapping("/{id}/update")
    @ResponseStatus(HttpStatus.OK)
    public StatusResponse<UpdateAdvertisementResponse> updateAdvertisement(
            @PathVariable Long id,
            @Valid @RequestPart("request") UpdateAdvertisementRequest updateRequest,
            @Nullable @RequestPart(value = "photos", required = false) MultipartFile[] photos,
            Principal principal) throws IOException {
        return advertisementUpdateService.updateAdvertisement(id, updateRequest, photos, principal.getName());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public StatusResponse<AdvertisementDto> getUserAdvertisement(@PathVariable Long id,
                                                                 Principal principal) {
        return advertisementManagementService.retrieveAdvertisementWithAuthorization(id, principal);
    }

    @GetMapping("/{id}/active")
    @ResponseStatus(HttpStatus.OK)
    public StatusResponse<AdvertisementDto> getAdvertisementById(@PathVariable Long id) {
        return advertisementManagementService.getActiveAdvertisement(id);
    }

    @GetMapping("/{id}/info")
    @ResponseStatus(HttpStatus.OK)
    public AdvertisementInfoForChatService getAdvertisementByIdForChatService(@PathVariable Long id) {
        return advertisementManagementService.getAdvertisementShortInfo(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAdvertisementById(@PathVariable Long id, Principal principal) throws IOException {
        advertisementManagementService.deleteAdvertisement(id, principal);
    }

    @PatchMapping("/{id}/change-status")
    @ResponseStatus(HttpStatus.OK)
    public StatusResponse<AdvertisementStatusResponse> enableAdvertisement(@PathVariable @Valid Long id) {
        return advertisementManagementService.setAdvertisementEnabledStatus(id);
    }
}
