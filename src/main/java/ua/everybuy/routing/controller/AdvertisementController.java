package ua.everybuy.routing.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ua.everybuy.buisnesslogic.service.AdvertisementService;
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
            HttpServletRequest request,
            @RequestPart(value = "photos", required = false) MultipartFile[] photos,
            Principal principal) throws IOException {

        StatusResponse response = advertisementService.createAdvertisement(createRequest, request, photos, principal.getName());
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<StatusResponse> getAdvertisementById(@PathVariable Long id, HttpServletRequest request) {
        StatusResponse response = advertisementService.getAdvertisement(id, request);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> deleteAdvertisementById(@PathVariable Long id) throws IOException {
        advertisementService.deleteAdvertisement(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/change-status")
    public ResponseEntity<StatusResponse> enableAdvertisement(@PathVariable @Valid Long id) {
        StatusResponse response = advertisementService.setAdvertisementEnabledStatus(id);
        return ResponseEntity.ok(response);
    }

}




