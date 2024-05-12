package ua.everybuy.routing.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ua.everybuy.buisnesslogic.service.AdvertisementService;
import ua.everybuy.routing.dto.StatusResponse;
import ua.everybuy.routing.dto.request.CreateAdvertisementRequest;

import java.io.IOException;

@RestController
@RequestMapping("/ad")
@RequiredArgsConstructor
public class AdvertisementController {

    private final AdvertisementService advertisementService;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<StatusResponse> createAdvertisement(
            @Valid @RequestPart("request") CreateAdvertisementRequest request,
            @RequestPart("photos") MultipartFile[] photos) throws IOException {

        StatusResponse response = advertisementService.createAdvertisement(request, photos);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<StatusResponse> getAdvertisementById(@PathVariable Long id) {
        StatusResponse response = advertisementService.getAdvertisement(id);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<StatusResponse> deleteAdvertisementById(@PathVariable Long id) throws IOException {
        StatusResponse response = advertisementService.deleteAdvertisement(id);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}




