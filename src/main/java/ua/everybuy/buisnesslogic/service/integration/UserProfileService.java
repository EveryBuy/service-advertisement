package ua.everybuy.buisnesslogic.service.integration;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ua.everybuy.routing.dto.*;
import ua.everybuy.routing.dto.response.UserShortInfoResponse;

@Service
@RequiredArgsConstructor
public class UserProfileService {
    private final ExchangeService exchangeService;
    private static final String SHORT_INFO_ENDPOINT = "/short-info?userId=";

    @Value("${user.service.url}")
    private String userServiceUrl;

    public ShortUserInfoDto getShortUserInfo(HttpServletRequest request, Long userId) {
        String shortUserInfoUrl = userServiceUrl + SHORT_INFO_ENDPOINT + userId;
        UserShortInfoResponse response = exchangeService
                .exchangeGetRequest(request, shortUserInfoUrl, UserShortInfoResponse.class).getBody();
        return response != null ? response.getData() : null;
    }
}
