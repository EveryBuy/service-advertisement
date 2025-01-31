package ua.everybuy.routing.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class ValidUserDto {
    private Boolean isValid;
    private Long userId;
    private String email;
    private String phoneNumber;
    private List<String> roles;
}
