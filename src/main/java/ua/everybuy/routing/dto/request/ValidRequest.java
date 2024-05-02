package ua.everybuy.routing.dto.request;

import lombok.Getter;
import lombok.Setter;
import ua.everybuy.routing.dto.UserInfoDto;

@Getter
@Setter
public class ValidRequest {
    private int status;
    private UserInfoDto data;

}