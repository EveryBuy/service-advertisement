package ua.everybuy.routing.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShortUserInfoDto{
    private long userId;
    private String fullName;
    private String photoUrl;
    private boolean online;
}
