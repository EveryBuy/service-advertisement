package ua.everybuy.routing.dto.response;

import lombok.Getter;
import ua.everybuy.routing.dto.ValidUserDto;

@Getter
public class ValidResponse {
    private int status;
    private ValidUserDto data;

}