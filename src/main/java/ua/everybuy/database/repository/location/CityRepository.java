package ua.everybuy.database.repository.location;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.everybuy.database.entity.City;
import java.util.List;
import java.util.Optional;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {
    @Query("SELECT c FROM City c JOIN FETCH c.region WHERE c.region.id = :regionId")
    List<City> findAllByRegionId(@Param("regionId") Long regionId);

    @Query("SELECT c FROM City c JOIN FETCH c.region")
    List<City> findAllWithRegions();

    @Query("SELECT c FROM City c JOIN FETCH c.region WHERE c.id = :id")
    Optional<City> findByIdWithRegion(Long id);

    @Query("SELECT c FROM City c JOIN FETCH c.region WHERE c.id = :cityId AND c.region.id = :regionId")
    Optional<City> findByCityIdAndRegionId(@Param("cityId") Long cityId, @Param("regionId") Long regionId);

}
