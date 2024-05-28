package ua.everybuy.database.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
    private String price;

    @Column(name = "creation_date", nullable = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime creationDate;

    @Column(name = "is_enabled")
    private Boolean isEnabled;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @ManyToOne
    @JoinColumn(name="city_id", nullable = false)
    private City city;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "subcategory_id", nullable = false)
    private SubCategory subCategory;

    @Enumerated(EnumType.STRING)
    @Column(name = "product_type", nullable = false, length = 10)
    private ProductType productType;

    public enum ProductType {
        NEW, USED
    }

    @ElementCollection(targetClass = DeliveryMethod.class, fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "advertisement_delivery_methods", joinColumns = @JoinColumn(name = "advertisement_id"))
    @Column(name = "delivery_method")
    private Set<DeliveryMethod> deliveryMethods = new HashSet<>();

    public enum DeliveryMethod {
        NOVA_POST,
        UKR_POST,
        MEEST_EXPRESS
    }
}
