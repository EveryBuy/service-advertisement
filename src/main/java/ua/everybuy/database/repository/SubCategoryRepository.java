package ua.everybuy.database.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import ua.everybuy.database.entity.SubCategory;

public interface SubCategoryRepository extends JpaRepository<SubCategory, Long> {
}
