package ua.everybuy.routing.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ua.everybuy.buisnesslogic.service.AdvertisementService;
import ua.everybuy.routing.dto.AdvertisementDto;
import ua.everybuy.routing.dto.request.UpdateAdvertisementRequest;
import ua.everybuy.routing.dto.response.*;
import ua.everybuy.routing.dto.request.CreateAdvertisementRequest;

import java.io.IOException;
import java.security.Principal;

@RestController
@RequestMapping("/ad")
@RequiredArgsConstructor
public class AdvertisementController {

    private final AdvertisementService advertisementService;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public StatusResponse<CreateAdvertisementResponse> createAdvertisement(
            @Valid @RequestPart("request") CreateAdvertisementRequest createRequest,
            @Nullable @RequestPart(value = "photos", required = false) MultipartFile[] photos,
            Principal principal) throws IOException {
        return advertisementService.createAdvertisement(createRequest, photos, principal.getName());
    }

    @PutMapping("/{id}/update")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public StatusResponse<UpdateAdvertisementResponse> updateAdvertisement(
            @PathVariable Long id,
            @Valid @RequestPart("request") UpdateAdvertisementRequest updateRequest,
            @Nullable @RequestPart(value = "photos", required = false) MultipartFile[] photos,
            Principal principal) throws IOException {
        return advertisementService.updateAdvertisement(id, updateRequest, photos, principal.getName());
    }

    @GetMapping("/{id}/active")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public StatusResponse<AdvertisementDto> getAdvertisementById(@PathVariable Long id, HttpServletRequest request) {
        return advertisementService.getAdvertisement(id, request);
    }

    @GetMapping("/{id}/info")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public AdvertisementInfoForChatService getAdvertisementById(@PathVariable Long id) {
        return advertisementService.getAdvertisementShortInfo(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAdvertisementById(@PathVariable Long id, Principal principal) throws IOException {
        advertisementService.deleteAdvertisement(id, principal);
    }

    @PutMapping("/{id}/change-status")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public StatusResponse<AdvertisementStatusResponse> enableAdvertisement(@PathVariable @Valid Long id) {
        return advertisementService.setAdvertisementEnabledStatus(id);
    }
}
