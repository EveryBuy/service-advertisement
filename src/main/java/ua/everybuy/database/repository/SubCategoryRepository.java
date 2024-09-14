package ua.everybuy.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ua.everybuy.database.entity.SubCategory;
import java.util.List;

public interface SubCategoryRepository extends JpaRepository<SubCategory, Long> {
    @Query("SELECT sc FROM SubCategory sc WHERE sc.category.id = :categoryId AND sc.parentCategory IS NULL")
    List<SubCategory> findTopLevelSubCategoriesByCategoryId(@Param("categoryId") Long categoryId);
    List<SubCategory> findByParentCategoryId(Long parentId);
}
