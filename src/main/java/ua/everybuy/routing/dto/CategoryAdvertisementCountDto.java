package ua.everybuy.routing.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CategoryAdvertisementCountDto {
    private long categoryId;
    private String nameUkr;
    private int count;
}
