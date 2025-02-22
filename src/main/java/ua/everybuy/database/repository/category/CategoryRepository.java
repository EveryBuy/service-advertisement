package ua.everybuy.database.repository.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.everybuy.database.entity.Category;

import java.util.List;


@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("SELECT c.nameUkr FROM Category c")
    List<String> findAllUkrCategories();
}