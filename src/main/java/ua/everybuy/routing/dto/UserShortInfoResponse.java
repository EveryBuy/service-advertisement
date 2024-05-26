package ua.everybuy.routing.dto;

import lombok.Getter;

@Getter
public class UserShortInfoResponse {
    private int status;
    private ShortUserInfoDto data;
}
