package ua.everybuy.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.everybuy.database.entity.AdvertisementPhoto;

import java.util.List;

@Repository
public interface AdvertisementPhotoRepository extends JpaRepository<AdvertisementPhoto, Long> {
    List<AdvertisementPhoto> findByAdvertisementId(Long advertisementId);

}
