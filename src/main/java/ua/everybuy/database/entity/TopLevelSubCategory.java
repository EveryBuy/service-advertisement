package ua.everybuy.database.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "top_level_subcategories")
public class TopLevelSubCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name_en", nullable = false, length = 100)
    private String subCategoryName;

    @Column(name = "name_ukr", nullable = false, length = 100)
    private String subCategoryNameUkr;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @OneToMany(mappedBy = "topLevelSubCategory", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LowLevelSubCategory> lowLevelSubCategories;
}
