package ua.everybuy.database.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "low_level_subcategories")
public class LowLevelSubCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name_en", nullable = false, length = 100)
    private String subCategoryName;

    @Column(name = "name_ukr", nullable = false, length = 100)
    private String subCategoryNameUkr;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "top_level_subcategory_id", nullable = false)
    private TopLevelSubCategory topLevelSubCategory;
}
