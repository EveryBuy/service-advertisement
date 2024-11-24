package ua.everybuy.routing.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ua.everybuy.buisnesslogic.service.advertisement.AdvertisementFavouriteService;
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
    private final AdvertisementFavouriteService advertisementFavouriteService;

    @PostMapping("/{id}/add-to-favourite")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public StatusResponse<AddToFavouriteResponse> addToFavourite(Principal principal,
                                                                 @PathVariable(name = "id") Long advertisementId) {
        return advertisementFavouriteService.addToFavorites(principal, advertisementId);
    }

    @DeleteMapping("/{id}/remove-from-favourite")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeFromFavourites(Principal principal,
                                     @PathVariable(name = "id") Long advertisementId) {
        advertisementFavouriteService.removeFromFavorites(principal, advertisementId);
    }

    @GetMapping("/favourite-ads")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public StatusResponse<List<FavouriteAdvertisementResponse>> getAllUsersFavouriteAdvertisementsWithCategory(
            @RequestParam(required = false) Long categoryId, Principal principal,
            @RequestParam(required = false) Advertisement.AdSection section) {
        List<FavouriteAdvertisementResponse> responseList = advertisementFavouriteService
                .findUserFavouriteAdvertisementsByCategoryAndSection(principal, categoryId, section);
        return new StatusResponse<>(HttpStatus.OK.value(), responseList);
    }
}
