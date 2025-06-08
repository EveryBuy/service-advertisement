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
    private Double price;
    private Date creationDate;
    private Date updateDate;
    private Boolean isEnabled;
    private Long userId;
    private String mainPhotoUrl;
    private Long cityId;
    private Long topSubCategoryId;
    private Long lowSubCategoryId;
    private String productType;
    private String section;
}
