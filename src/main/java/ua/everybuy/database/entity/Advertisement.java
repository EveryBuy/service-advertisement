package ua.everybuy.database.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "advertisements")
public class Advertisement {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "title", nullable = false)
    private String title;
    @Column(name = "description", nullable = false, length = 1000)
    private String description;
    @Column(name = "price", nullable = false, length = 55)
    private String price;

    @Column(name = "creation_date", nullable = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime creationDate;

    @Column(name = "is_enabled", insertable = false)
    private Boolean isEnabled;
    @Column(name = "user_id", nullable = false)
    private Long userId;
    @ManyToOne
    @JoinColumn(name="city_id", nullable = false)
    private City city;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subcategory_id", nullable = false)
    private SubCategory subCategory;
    @OneToMany(mappedBy = "advertisement", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<AdvertisementPhoto> photos;

    @Enumerated(EnumType.STRING)
    @Column(name = "product_type", nullable = false, length = 10)
    private ProductType productType;

    private enum ProductType {
        NEW, USED
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_method", nullable = false, length = 25)
    private DeliveryMethod deliveryMethod;

    private enum DeliveryMethod {
        NOVA_POST,
        UKR_POST,
        MEEST_EXPRESS
    }
}