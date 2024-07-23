package ua.everybuy.routing.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ua.everybuy.buisnesslogic.service.FavouriteAdvertisementService;
import ua.everybuy.routing.dto.response.AddToFavouriteResponse;
import ua.everybuy.routing.dto.response.FavouriteAdvertisementResponse;
import ua.everybuy.routing.dto.response.StatusResponse;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/ad")
@RequiredArgsConstructor
public class FavouriteAdvertisementController {
    private final FavouriteAdvertisementService favouriteAdvertisementService;

    @PostMapping("/{id}/add-to-favourite")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public StatusResponse<AddToFavouriteResponse> addToFavourite(Principal principal,
                                                                 @PathVariable(name = "id") Long advertisementId) {
        return favouriteAdvertisementService.addToFavorites(principal.getName(), advertisementId);
    }

    @DeleteMapping("/{id}/remove-from-favourite")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeFromFavourites(Principal principal,
                                     @PathVariable(name = "id") Long advertisementId) {
        favouriteAdvertisementService.removeFromFavorites(principal.getName(), advertisementId);
    }

    @GetMapping("/favourite-ads")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public StatusResponse<List<FavouriteAdvertisementResponse>> getAllUsersFavouriteAdvertisementsWithCategory(
            @RequestParam(required = false) Long categoryId, Principal principal) {
        List<FavouriteAdvertisementResponse> responseList = favouriteAdvertisementService
                .findAllUserFavouriteAdvertisementsWithCategory(principal.getName(), categoryId);
        return new StatusResponse<>(HttpStatus.OK.value(), responseList);
    }
}
