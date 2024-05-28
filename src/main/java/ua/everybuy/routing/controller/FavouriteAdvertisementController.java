package ua.everybuy.routing.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.everybuy.buisnesslogic.service.FavouriteAdvertisementService;
import ua.everybuy.database.entity.Advertisement;
import ua.everybuy.routing.dto.response.StatusResponse;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/ad")
@RequiredArgsConstructor
public class FavouriteAdvertisementController {
    private final FavouriteAdvertisementService favouriteAdvertisementService;

    @PostMapping("/{id}/add-to-favourite")
    public ResponseEntity<StatusResponse> addToFavourite(Principal principal,
                                                         @PathVariable(name = "id") Long advertisementId){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(favouriteAdvertisementService.addToFavorites(principal.getName(), advertisementId));
    }

    @DeleteMapping("/{id}/remove-from-favourite")
    public ResponseEntity<?> removeFromFavourites(Principal principal,
                                                  @PathVariable(name = "id") Long advertisementId) {
        favouriteAdvertisementService.removeFromFavorites(principal.getName(), advertisementId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/favourite-ads")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<StatusResponse> getAllUsersFavouriteAdvertisements(Principal principal) {
        List<Advertisement> favouriteUsersAdvertisements = favouriteAdvertisementService.findAllUserFavouriteAdvertisements(principal.getName());

        return ResponseEntity.ok(StatusResponse.builder()
                .status(HttpStatus.OK.value())
                .data(favouriteUsersAdvertisements)
                .build());
    }

}
