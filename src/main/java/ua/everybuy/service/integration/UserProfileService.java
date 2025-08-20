package ua.everybuy.service.integration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import ua.everybuy.routing.dto.*;
import ua.everybuy.routing.dto.response.UserShortInfoResponse;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserProfileService {
    @Value("${user.service.url}")
    private String userServiceUrl;
    @Value("${user.default.avatar.url}")
    private String defaultAvatarUrl;
    private final ExchangeService exchangeService;

    @Cacheable(value = "shortUserInfo", key = "#userId")
    public ShortUserInfoDto getShortUserInfo(Long userId) {
        try {
            String shortUserInfoUrl = buildShortUserInfoUrl(userId);
            UserShortInfoResponse response = exchangeService
                    .exchangeGetRequest(shortUserInfoUrl, UserShortInfoResponse.class).getBody();

            return response != null ? response.getData() : createDefaultShortUserInfo(userId);
        } catch (Exception e) {
            log.error("Failed to fetch short user info for userId={}, "
                            + "returning default user info. Error: {}",
                    userId, e.getMessage(), e);
            return createDefaultShortUserInfo(userId);
        }
    }

    private ShortUserInfoDto createDefaultShortUserInfo(Long userId) {
        return ShortUserInfoDto.builder()
                .userId(userId)
                .fullName("Unknown User")
                .photoUrl(defaultAvatarUrl)
                .online(false)
                .build();
    }

    private String buildShortUserInfoUrl(Long userId) {
        return UriComponentsBuilder.fromHttpUrl(userServiceUrl)
                .path("/short-info")
                .queryParam("userId", userId)
                .toUriString();
    }
}
