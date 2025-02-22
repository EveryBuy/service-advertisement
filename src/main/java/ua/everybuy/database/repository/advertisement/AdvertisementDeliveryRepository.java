package ua.everybuy.database.repository.advertisement;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.everybuy.database.entity.Advertisement;
import ua.everybuy.database.entity.AdvertisementDelivery;
import java.util.Set;

@Repository
public interface AdvertisementDeliveryRepository extends JpaRepository<AdvertisementDelivery, Long> {

    @Modifying
    @Transactional
    @Query("DELETE FROM AdvertisementDelivery ad WHERE ad.advertisement = :advertisement")
    void deleteAllByAdvertisement(@Param("advertisement") Advertisement advertisement);
    Set<AdvertisementDelivery> findByAdvertisement(Advertisement advertisement);
}
