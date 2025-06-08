package ua.everybuy.database.repository.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.everybuy.database.entity.TopLevelSubCategory;
import java.util.List;

@Repository
public interface TopLevelSubCategoryRepository extends JpaRepository<TopLevelSubCategory, Long> {
    List<TopLevelSubCategory> findByCategoryId(Long categoryId);

    List<TopLevelSubCategory> findByIdIn(List<Long> ids);
}

