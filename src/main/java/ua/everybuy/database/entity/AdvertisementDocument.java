package ua.everybuy.database.entity;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdvertisementDocument {
    private Long id;
    private String title;
    private String description;
    private Long price;
    private Date creationDate;
    private Date updateDate;
    private Boolean isEnabled;
    private Boolean isNegotiable;
    private Long userId;
    private String mainPhotoUrl;
    private Long cityId;
    private Long categoryId;
    private Long topSubCategoryId;
    private Long lowSubCategoryId;
    private String productType;
    private String section;
}
