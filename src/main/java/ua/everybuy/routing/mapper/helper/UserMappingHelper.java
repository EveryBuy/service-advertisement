package ua.everybuy.routing.mapper.helper;

import lombok.RequiredArgsConstructor;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;
import ua.everybuy.buisnesslogic.service.integration.UserProfileService;
import ua.everybuy.database.entity.Advertisement;
import ua.everybuy.routing.dto.ShortUserInfoDto;

@Component
@RequiredArgsConstructor
public class UserMappingHelper {
    private final UserProfileService userProfileService;

    @Named("getUserInfo")
    public ShortUserInfoDto getUserInfo(Advertisement advertisement) {
        return userProfileService.getShortUserInfo(advertisement.getUserId());
    }
}
