package ua.everybuy.database.repository;

//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//
//import java.util.List;
//import java.util.Optional;
//
//public interface SubCategoryRepository extends JpaRepository<SubCategory, Long> {
//    @Query("SELECT sc FROM SubCategory sc WHERE sc.category.id = :categoryId AND sc.parentCategory IS NULL")
//    List<SubCategory> findTopLevelSubCategoriesByCategoryId(@Param("categoryId") Long categoryId);
//    @Query("SELECT sc FROM SubCategory sc WHERE sc.id = :id AND sc.parentCategory IS NULL")
//    Optional<SubCategory> findTopLevelSubCategoryById(@Param("id") Long id);
//    List<SubCategory> findByParentCategoryId(Long parentId);
//}
