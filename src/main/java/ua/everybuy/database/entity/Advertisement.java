package ua.everybuy.database.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Table(name = "advertisements")
public class Advertisement {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false, length = 3000)
    private String description;

    @Column(name = "price", nullable = false, length = 55)
    private Long price;

    @Column(name = "creation_date", nullable = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime creationDate;

    @Column(name = "update_date", nullable = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime updateDate;

    @Column(name = "is_enabled")
    private Boolean isEnabled;

    @Column(name = "is_negotiable", nullable = false)
    private Boolean isNegotiable = false;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "main_photo_url")
    private String mainPhotoUrl;

    @Embedded
    private AdvertisementStatistics statistics;

    @ManyToOne
    @JoinColumn(name = "city_id", nullable = false)
    private City city;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "top_level_subcategory_id", nullable = false)
    private TopLevelSubCategory topSubCategory;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "low_level_subcategory_id")
    private LowLevelSubCategory lowSubCategory;

    @Enumerated(EnumType.STRING)
    @Column(name = "product_type", nullable = false, length = 10)
    private ProductType productType;

    public enum ProductType {
        NEW, USED, OTHER
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "ad_section", nullable = false, length = 10)
    private AdSection section;

    public enum AdSection {
        BUY, SELL
    }

    @OneToMany(mappedBy = "advertisement", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<AdvertisementDelivery> advertisementDeliveries;

    @OneToMany(mappedBy = "advertisement", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<FavouriteAdvertisement> favouriteAdvertisements;
}
