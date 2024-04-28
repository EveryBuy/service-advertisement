package ua.everybuy.database.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "subcategories")
public class SubCategory {

    @Id
    private Long id;

    @Column(name = "name_en", nullable = false, length = 100)
    private String categoryName;

    @Column(name = "name_ukr", nullable = false, length = 100)
    private String nameUkr;
    @ManyToOne
    @JoinColumn(name="category_id")
    private Category category;
}
