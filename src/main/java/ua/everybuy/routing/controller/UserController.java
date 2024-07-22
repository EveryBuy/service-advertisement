package ua.everybuy.routing.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.everybuy.buisnesslogic.service.UserAdvertisementService;
import ua.everybuy.routing.dto.response.StatusResponse;

import java.security.Principal;

@RestController
@RequestMapping("/ad")
@RequiredArgsConstructor
public class UserController {
    private final UserAdvertisementService userAdvertisementService;
    @GetMapping("/user/active-ads")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<StatusResponse> getAllActiveUsersAds(Principal principal) {
        return ResponseEntity.status(HttpStatus.OK).body(StatusResponse.builder()
                .status(HttpStatus.OK.value())
                .data(userAdvertisementService.getUserAdvertisementsByEnabledStatus(Long.parseLong(principal.getName()), true))
                .build());
    }

    @GetMapping("/user/inactive-ads")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<StatusResponse> getAllNotActiveUsersAds(Principal principal) {
        return ResponseEntity.status(HttpStatus.OK).body(StatusResponse.builder()
                .status(HttpStatus.OK.value())
                .data(userAdvertisementService.getUserAdvertisementsByEnabledStatus(Long.parseLong(principal.getName()), false))
                .build());
    }

    @GetMapping("/{id}/user")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<StatusResponse> getUserAdvertisement(@PathVariable Long id,
                                                               HttpServletRequest request,
                                                               Principal principal) {
        StatusResponse response = userAdvertisementService.getUserAdvertisement(id, request, principal);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

}
