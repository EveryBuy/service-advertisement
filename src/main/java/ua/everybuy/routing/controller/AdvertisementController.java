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
    public ResponseEntity<StatusResponse> createAdvertisement(
            @Valid @RequestPart("request") CreateAdvertisementRequest request,
            @RequestPart("photos") MultipartFile[] photos) throws IOException {

        StatusResponse response = advertisementService.createAdvertisement(request, photos);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}




