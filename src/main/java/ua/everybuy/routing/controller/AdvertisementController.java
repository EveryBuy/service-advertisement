package ua.everybuy.routing.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ua.everybuy.buisnesslogic.service.AdvertisementService;
import ua.everybuy.buisnesslogic.service.UserService;
import ua.everybuy.routing.dto.ShortUserInfoDto;
import ua.everybuy.routing.dto.StatusResponse;
import ua.everybuy.routing.dto.request.CreateAdvertisementRequest;

import java.io.IOException;
import java.security.Principal;

@RestController
@RequestMapping("/ad")
@RequiredArgsConstructor
public class AdvertisementController {

    private final AdvertisementService advertisementService;
    private final UserService userService;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<StatusResponse> createAdvertisement(
            @Valid @RequestPart("request") CreateAdvertisementRequest request,
            @RequestPart("photos") MultipartFile[] photos, Principal principal) throws IOException {

        StatusResponse response = advertisementService.createAdvertisement(request, photos, principal.getName());
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
    public void deleteAdvertisementById(@PathVariable Long id) throws IOException {
        advertisementService.deleteAdvertisement(id);
    }

    @PutMapping("/{id}/enable")
    public ResponseEntity<StatusResponse> enableAdvertisement(@PathVariable Long id) {
        StatusResponse response = advertisementService.enableAdvertisement(id);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

    @PutMapping("/{id}/disable")
    public ResponseEntity<StatusResponse> disableAdvertisement(@PathVariable Long id) {
        StatusResponse response = advertisementService.disableAdvertisement(id);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

    @GetMapping("/get-all-active-users-ads")
    public ResponseEntity<StatusResponse> getAllActiveUsersAds(Principal principal){
        return ResponseEntity.status(HttpStatus.OK).body(StatusResponse.builder()
                .status(200)
                .data(advertisementService.getNeededUsersAdvertisements(principal.getName(), true))
                .build());
    }

    @GetMapping("/get-all-not-active-users-ads")
    public ResponseEntity<StatusResponse> getAllNotActiveUsersAds(Principal principal){
        return ResponseEntity.status(HttpStatus.OK).body(StatusResponse.builder()
                .status(200)
                .data(advertisementService.getNeededUsersAdvertisements(principal.getName(), false))
                .build());
    }
}




