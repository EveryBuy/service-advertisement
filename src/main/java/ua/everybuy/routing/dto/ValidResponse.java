package ua.everybuy.routing.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValidResponse {
    private int status;
    private ValidUserDto data;

}