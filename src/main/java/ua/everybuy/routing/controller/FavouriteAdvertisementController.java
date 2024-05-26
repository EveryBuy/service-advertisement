package ua.everybuy.routing.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.everybuy.buisnesslogic.service.FavouriteAdvertisementService;
import ua.everybuy.routing.dto.StatusResponse;

import java.security.Principal;

@RestController
@RequestMapping("/ad/favourite")
@RequiredArgsConstructor
public class FavouriteAdvertisementController {
    private final FavouriteAdvertisementService favouriteAdvertisementService;

    @PostMapping()
    public ResponseEntity<StatusResponse> addToFavourite(Principal principal,
                                                         @RequestParam(name = "advertisementId") long advertisementId){
        return ResponseEntity.ok(favouriteAdvertisementService.addAdvToFavourite(principal.getName(), advertisementId));
    }

    @DeleteMapping()
    public ResponseEntity<StatusResponse> removeFromFavourites(Principal principal,
                                                         @RequestParam(name = "advertisementId") long advertisementId){
        return ResponseEntity.ok(favouriteAdvertisementService.removeAdvertisement(principal.getName(), advertisementId));
    }
}
