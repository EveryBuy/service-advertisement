package ua.everybuy.routing.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ua.everybuy.buisnesslogic.service.AdvertisementService;
import ua.everybuy.routing.dto.response.StatusResponse;

import java.security.Principal;

@RestController
@RequestMapping("/ad/user")
@RequiredArgsConstructor
public class UserController {
    private final AdvertisementService advertisementService;

    @GetMapping("/active-ads")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<StatusResponse> getAllActiveUsersAds(Principal principal) {
        return ResponseEntity.status(HttpStatus.OK).body(StatusResponse.builder()
                .status(HttpStatus.OK.value())
                .data(advertisementService.getUserAdvertisementsByEnabledStatus(principal.getName(), true))
                .build());
    }

    @GetMapping("/inactive-ads")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<StatusResponse> getAllNotActiveUsersAds(Principal principal) {
        return ResponseEntity.status(HttpStatus.OK).body(StatusResponse.builder()
                .status(HttpStatus.OK.value())
                .data(advertisementService.getUserAdvertisementsByEnabledStatus(principal.getName(), false))
                .build());
    }

    @GetMapping("/all-ads")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<StatusResponse> getAllUsersAds(Principal principal) {
        return ResponseEntity.status(HttpStatus.OK).body(StatusResponse.builder()
                .status(HttpStatus.OK.value())
                .data(advertisementService.findAllUserAdvertisement(principal.getName()))
                .build());
    }
}