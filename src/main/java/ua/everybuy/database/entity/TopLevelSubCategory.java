package ua.everybuy.database.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "top_level_subcategories")
@NamedEntityGraph(
        name = "TopLevelSubCategory.withLowLevels",
        attributeNodes = @NamedAttributeNode("lowLevelSubCategories")
)
@AllArgsConstructor
@NoArgsConstructor
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

    @OneToMany(mappedBy = "topLevelSubCategory", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<LowLevelSubCategory> lowLevelSubCategories;
}
