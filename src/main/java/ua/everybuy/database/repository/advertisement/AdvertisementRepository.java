package ua.everybuy.database.repository.advertisement;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.everybuy.database.entity.Advertisement;
import java.util.List;
import java.util.Optional;

@Repository("advertisementRepository")
public interface AdvertisementRepository extends JpaRepository<Advertisement, Long>,
        JpaSpecificationExecutor<Advertisement> {
    Optional<Advertisement> findByIdAndUserId(Long id, Long userId);

    @Query("SELECT a FROM Advertisement a " +
            "JOIN FETCH a.advertisementDeliveries " +
            "JOIN FETCH a.city " +
            "WHERE a.userId = :userId " +
            "AND (:section IS NULL OR a.section = :section) " +
            "AND (a.isEnabled = :isEnabled) ")
    List<Advertisement> findByUserId(Long userId, Boolean isEnabled,
                                     Advertisement.AdSection section,
                                     Pageable pageable);

    @EntityGraph(attributePaths = {"city", "lowLevelSubcategory", "topLevelSubcategory", "advertisementDeliveries"})
    List<Advertisement> findByIsEnabledTrueOrderByCreationDateDesc();
}
