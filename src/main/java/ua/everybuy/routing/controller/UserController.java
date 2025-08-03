package ua.everybuy.routing.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ua.everybuy.database.entity.Advertisement;
import ua.everybuy.database.entity.City;
import ua.everybuy.routing.dto.UserAdvertisementDto;
import ua.everybuy.routing.dto.response.AdvertisementWithStatisticResponse;
import ua.everybuy.routing.dto.response.StatusResponse;
import ua.everybuy.service.advertisement.user.UserDeletionService;
import ua.everybuy.service.advertisement.user.UserLastLocationService;
import ua.everybuy.service.advertisement.user.UserProfileService;
import ua.everybuy.service.advertisement.user.UserStatisticService;
import java.io.IOException;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/product/user")
@RequiredArgsConstructor
public class UserController {
    private final UserStatisticService userStatisticService;
    private final UserProfileService userProfileService;
    private final UserDeletionService userDeletionService;
    private final UserLastLocationService userLastLocationService;

    @GetMapping("/active-ads")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public StatusResponse<List<AdvertisementWithStatisticResponse>> getAllActiveUsersAds(
            Principal principal, @RequestParam(required = false, defaultValue = "SELL") @Valid Advertisement.AdSection section,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "8") int size) {
        List<AdvertisementWithStatisticResponse> responseList = userStatisticService
                .getUserAdvertisementsWithStatistic(Long.parseLong(principal.getName()), true, section, page, size);
        return new StatusResponse<>(HttpStatus.OK.value(), responseList);
    }

    @GetMapping("/inactive-ads")
    @ResponseStatus(HttpStatus.OK)
    public StatusResponse<List<AdvertisementWithStatisticResponse>> getAllNotActiveUsersAds(
            Principal principal, @RequestParam(required = false, defaultValue = "SELL") @Valid Advertisement.AdSection section,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "8") int size) {
        List<AdvertisementWithStatisticResponse> responseList = userStatisticService
                .getUserAdvertisementsWithStatistic(Long.parseLong(principal.getName()), false, section, page, size);
        return new StatusResponse<>(HttpStatus.OK.value(), responseList);
    }

    @GetMapping("/{userId}/ads")
    @ResponseStatus(HttpStatus.OK)
    public UserAdvertisementDto getUserAds(@PathVariable Long userId, @RequestParam(required = false) Long categoryId,
                                           @RequestParam(required = false, defaultValue = "SELL")
                                           Advertisement.AdSection section,
                                           @RequestParam(defaultValue = "1") int page,
                                           @RequestParam(defaultValue = "20") int size) {

        return userProfileService.getUserActiveFilteredAdvertisements(userId, categoryId, section, page, size);
    }

    @DeleteMapping("/{userId}/ads")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUserAdvertisements(@PathVariable Long userId,
                                         HttpServletRequest request) throws IOException {
        userDeletionService.deleteAllAndPushUserAdvertisements(userId, request);
    }

    @GetMapping("/last-location")
    @ResponseStatus(HttpStatus.OK)
    public City getUserLastLocation(Principal principal) {
        return userLastLocationService.getLastLocationForUser(Long.parseLong(principal.getName()));
    }
}
