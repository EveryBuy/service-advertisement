package ua.everybuy.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.everybuy.database.entity.LowLevelSubCategory;
import java.util.List;

public interface LowLevelSubCategoryRepository extends JpaRepository<LowLevelSubCategory, Long> {
    List<LowLevelSubCategory> findByTopLevelSubCategoryId(Long topLevelSubCategoryId);
}
