package ua.everybuy.database.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table (name = "cities")
public class City {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column (name="city_name")
    private String cityName;
    @ManyToOne
    @JoinColumn (name="region_id")
    private Region region;
}
