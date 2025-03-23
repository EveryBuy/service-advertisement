package ua.everybuy.routing.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ua.everybuy.buisnesslogic.service.advertisement.AdvertisementUserService;
import ua.everybuy.database.entity.Advertisement;
import ua.everybuy.routing.dto.response.AdvertisementWithStatisticResponse;
import ua.everybuy.routing.dto.response.StatusResponse;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/ad")
@RequiredArgsConstructor
public class UserController {
    private final AdvertisementUserService advertisementUserService;

    @GetMapping("/user/active-ads")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public StatusResponse<List<AdvertisementWithStatisticResponse>> getAllActiveUsersAds(
            Principal principal, @RequestParam(required = false, defaultValue = "SELL") @Valid Advertisement.AdSection section,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "8") int size) {
        List<AdvertisementWithStatisticResponse> responseList = advertisementUserService
                .getUserAdvertisements(Long.parseLong(principal.getName()), true, section, page, size);
        return new StatusResponse<>(HttpStatus.OK.value(), responseList);
    }

    @GetMapping("/user/inactive-ads")
    @ResponseStatus(HttpStatus.OK)
    public StatusResponse<List<AdvertisementWithStatisticResponse>> getAllNotActiveUsersAds(
            Principal principal, @RequestParam(required = false, defaultValue = "SELL") @Valid Advertisement.AdSection section,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "8") int size) {
        List<AdvertisementWithStatisticResponse> responseList = advertisementUserService
                .getUserAdvertisements(Long.parseLong(principal.getName()), false, section, page, size);
        return new StatusResponse<>(HttpStatus.OK.value(), responseList);
    }
}
