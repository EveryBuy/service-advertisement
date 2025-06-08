package ua.everybuy.database.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Document(indexName = "advertisements")
public class AdvertisementDocument {
    @Id
    private Long id;

    @Field(type = FieldType.Text, analyzer = "ukrainian")
    private String title;

    @Field(type = FieldType.Text, analyzer = "ukrainian")
    private String description;

    @Field(type = FieldType.Double)
    private Double price;

    @Field(type = FieldType.Date)
    private Date creationDate;

    @Field(type = FieldType.Date)
    private Date updateDate;

    @Field(type = FieldType.Boolean)
    private Boolean isEnabled;

    @Field(type = FieldType.Long)
    private Long userId;

    @Field(type = FieldType.Text)
    private String mainPhotoUrl;

    @Field(type = FieldType.Long)
    private Long cityId;

    @Field(type = FieldType.Long)
    private Long topSubCategoryId;

    @Field(type = FieldType.Long)
    private Long lowSubCategoryId;

    @Field(type = FieldType.Keyword)
    private String productType;

    @Field(type = FieldType.Keyword)
    private String section;
}
