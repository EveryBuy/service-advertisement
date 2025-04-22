package ua.everybuy.routing.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ShortUserInfoDto{
    private long userId;
    private String fullName;
    private String photoUrl;
}
