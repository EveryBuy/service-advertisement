package ua.everybuy.routing.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ua.everybuy.buisnesslogic.service.advertisement.UserAdvertisementService;
import ua.everybuy.routing.dto.AdvertisementDto;
import ua.everybuy.routing.dto.response.AdvertisementWithStatisticResponse;
import ua.everybuy.routing.dto.response.StatusResponse;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/ad")
@RequiredArgsConstructor
public class UserController {
    private final UserAdvertisementService userAdvertisementService;

    @GetMapping("/user/active-ads")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public StatusResponse<List<AdvertisementWithStatisticResponse>> getAllActiveUsersAds(Principal principal) {
        List<AdvertisementWithStatisticResponse> responseList = userAdvertisementService
                .getUserAdvertisementsByEnabledStatus(Long.parseLong(principal.getName()), true);
        return new StatusResponse<>(HttpStatus.OK.value(), responseList);
    }

    @GetMapping("/user/inactive-ads")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public StatusResponse<List<AdvertisementWithStatisticResponse>> getAllNotActiveUsersAds(Principal principal) {
        List<AdvertisementWithStatisticResponse> responseList = userAdvertisementService
                .getUserAdvertisementsByEnabledStatus(Long.parseLong(principal.getName()), false);
        return new StatusResponse<>(HttpStatus.OK.value(), responseList);
    }

    @GetMapping("/{id}/user")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody()
    public StatusResponse<AdvertisementDto> getUserAdvertisement(@PathVariable Long id,
                                                                 Principal principal) {
        return userAdvertisementService.getUserAdvertisement(id, principal);
    }
}
