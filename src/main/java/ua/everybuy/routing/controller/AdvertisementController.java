package ua.everybuy.routing.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ua.everybuy.buisnesslogic.service.AdvertisementService;
import ua.everybuy.routing.dto.request.UpdateAdvertisementRequest;
import ua.everybuy.routing.dto.response.StatusResponse;
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
    public ResponseEntity<StatusResponse> createAdvertisement(
            @Valid @RequestPart("request") CreateAdvertisementRequest createRequest,
            @Nullable @RequestPart(value = "photos", required = false) MultipartFile[] photos,
            Principal principal) throws IOException {

        StatusResponse response = advertisementService.createAdvertisement(createRequest, photos, principal.getName());
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("/{id}/update")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<StatusResponse> updateAdvertisement(
            @PathVariable Long id,
            @Valid @RequestPart("request") UpdateAdvertisementRequest updateRequest,
            @Nullable @RequestPart(value = "photos", required = false) MultipartFile[] photos,
            Principal principal) throws IOException {

        StatusResponse response = advertisementService.updateAdvertisement(id, updateRequest, photos, principal.getName());
        return ResponseEntity.status(response.getStatus()).body(response);
    }


    @GetMapping("/{id}/active")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<StatusResponse> getAdvertisementById(@PathVariable Long id, HttpServletRequest request) {
        StatusResponse response = advertisementService.getActiveAdvertisement(id, request);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/{id}/user")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<StatusResponse> getUserAdvertisement(@PathVariable Long id,
                                                               HttpServletRequest request,
                                                               Principal principal) {
        StatusResponse response = advertisementService.getUserAdvertisement(id, request, principal);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> deleteAdvertisementById(@PathVariable Long id, Principal principal) throws IOException {
        advertisementService.deleteAdvertisement(id, principal);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/change-status")
    public ResponseEntity<StatusResponse> enableAdvertisement(@PathVariable @Valid Long id) {
        StatusResponse response = advertisementService.setAdvertisementEnabledStatus(id);
        return ResponseEntity.ok(response);
    }

}




