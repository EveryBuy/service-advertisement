package ua.everybuy.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.everybuy.database.entity.LowLevelSubCategory;

import java.util.List;

@Repository
public interface LowLevelSubCategoryRepository extends JpaRepository<LowLevelSubCategory, Long> {
    List<LowLevelSubCategory> findByTopLevelSubCategoryId(Long topLevelSubCategoryId);
}
