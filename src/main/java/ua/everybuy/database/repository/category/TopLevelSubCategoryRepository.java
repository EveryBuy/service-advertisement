package ua.everybuy.database.repository.category;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.everybuy.database.entity.TopLevelSubCategory;
import java.util.List;
import java.util.Optional;

@Repository
public interface TopLevelSubCategoryRepository extends JpaRepository<TopLevelSubCategory, Long> {
    @EntityGraph(value = "TopLevelSubCategory.withLowLevels", type = EntityGraph.EntityGraphType.LOAD)
    List<TopLevelSubCategory> findByCategoryId(Long categoryId);

    @EntityGraph(value = "TopLevelSubCategory.withLowLevels", type = EntityGraph.EntityGraphType.LOAD)
    List<TopLevelSubCategory> findByIdIn(List<Long> ids);

    @EntityGraph(value = "TopLevelSubCategory.withLowLevels", type = EntityGraph.EntityGraphType.LOAD)
    Optional<TopLevelSubCategory> findById(Long id);
}
