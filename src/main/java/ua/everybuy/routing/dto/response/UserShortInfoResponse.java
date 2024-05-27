package ua.everybuy.routing.dto.response;

import lombok.Getter;
import ua.everybuy.routing.dto.ShortUserInfoDto;

@Getter
public class UserShortInfoResponse {
    private int status;
    private ShortUserInfoDto data;
}
