package ua.everybuy.routing.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class UserInfoDto {

    private Boolean isValid;
    private Long userId;
    private String email;
    private String phoneNumber;
    private List<String> roles;
}
