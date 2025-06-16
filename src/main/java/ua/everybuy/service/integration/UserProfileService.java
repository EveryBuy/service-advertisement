package ua.everybuy.service.integration;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ua.everybuy.routing.dto.*;
import ua.everybuy.routing.dto.response.UserShortInfoResponse;

@Service
@RequiredArgsConstructor
public class UserProfileService {
    private static final String SHORT_INFO_ENDPOINT = "/short-info?userId=";
    @Value("${user.service.url}")
    private String userServiceUrl;
    @Value("${user.default.avatar.url}")
    private String defaultAvatarUrl;
    private final ExchangeService exchangeService;

    public ShortUserInfoDto getShortUserInfo(Long userId) {
        try {
            String shortUserInfoUrl = userServiceUrl + SHORT_INFO_ENDPOINT + userId;
            UserShortInfoResponse response = exchangeService
                    .exchangeGetRequest(shortUserInfoUrl, UserShortInfoResponse.class).getBody();

            return response != null ? response.getData() : createDefaultShortUserInfo(userId);
        } catch (Exception e) {
            return createDefaultShortUserInfo(userId);
        }
    }

    private ShortUserInfoDto createDefaultShortUserInfo(Long userId) {
        return new ShortUserInfoDto(userId, "Unknown User",
                defaultAvatarUrl);
    }
}
