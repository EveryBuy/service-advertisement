package ua.everybuy.database.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.everybuy.database.entity.Advertisement;
import ua.everybuy.routing.dto.PriceRangeDto;
import java.util.List;
import java.util.Optional;

@Repository("advertisementRepository")
public interface AdvertisementRepository extends JpaRepository<Advertisement, Long>,
        JpaSpecificationExecutor<Advertisement> {
    Optional<Advertisement> findByIdAndUserId(Long id, Long userId);

    @Query("SELECT a FROM Advertisement a " +
            "JOIN FETCH a.advertisementDeliveries " +
            "WHERE a.userId = :userId")
    List<Advertisement> findByUserId(Long userId);

    @EntityGraph(attributePaths = {"city", "lowLevelSubcategory", "topLevelSubcategory", "advertisementDeliveries"})
    List<Advertisement> findByIsEnabledTrueOrderByCreationDateDesc();

    @Query("SELECT new ua.everybuy.routing.dto.PriceRangeDto(MIN(a.price), MAX(a.price)) " +
            "FROM Advertisement a " +
            "WHERE (:categoryId IS NULL OR a.topSubCategory.category.id = :categoryId) " +
            "AND (:cityId IS NULL OR a.city.id = :cityId) " +
            "AND (:regionId IS NULL OR a.city.region.id= :regionId) " +
            "AND (:topSubCategoryId IS NULL OR a.topSubCategory.id = :topSubCategoryId) " +
            "AND (:lowSubCategoryId IS NULL OR a.lowSubCategory.id = :lowSubCategoryId) " +
            "AND (:productType IS NULL OR a.productType = :productType) " +
            "AND (:section IS NULL OR a.section = :section)")
    Optional<PriceRangeDto> findMinAndMaxPrice(@Param("cityId") Long cityId,
                                               @Param("regionId") Long regionId,
                                               @Param("categoryId") Long categoryId,
                                               @Param("topSubCategoryId") Long topSubCategoryId,
                                               @Param("lowSubCategoryId") Long lowSubCategoryId,
                                               @Param("productType") Advertisement.ProductType productType,
                                               @Param("section") Advertisement.AdSection section);

}
