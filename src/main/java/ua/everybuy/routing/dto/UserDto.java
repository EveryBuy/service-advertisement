package ua.everybuy.routing.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {

    private Long userId;
    private String fullName;
    private String phone;
    private String email;
    private String userPhotoUrl;
}