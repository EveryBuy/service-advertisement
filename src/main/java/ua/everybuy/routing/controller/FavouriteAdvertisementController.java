package ua.everybuy.routing.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ua.everybuy.buisnesslogic.service.advertisement.FavouriteAdvertisementService;
import ua.everybuy.database.entity.Advertisement;
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
        return favouriteAdvertisementService.addToFavorites(principal, advertisementId);
    }

    @DeleteMapping("/{id}/remove-from-favourite")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeFromFavourites(Principal principal,
                                     @PathVariable(name = "id") Long advertisementId) {
        favouriteAdvertisementService.removeFromFavorites(principal, advertisementId);
    }

    @GetMapping("/favourite-ads")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public StatusResponse<List<FavouriteAdvertisementResponse>>
    getAllUsersFavouriteAdvertisementsWithCategory(Principal principal,
                                                   @RequestParam(required = false) @Valid Long categoryId,
                                                   @RequestParam(required = false, defaultValue = "SELL") @Valid Advertisement.AdSection section,
                                                   @RequestParam(defaultValue = "1") int page,
                                                   @RequestParam(defaultValue = "20") int size) {
        List<FavouriteAdvertisementResponse> responseList = favouriteAdvertisementService
                .getUserFavouriteAdvertisements(principal, categoryId, section, page, size);
        return new StatusResponse<>(HttpStatus.OK.value(), responseList);
    }
}
