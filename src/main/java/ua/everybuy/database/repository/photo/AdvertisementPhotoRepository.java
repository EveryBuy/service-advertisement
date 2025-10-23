package ua.everybuy.database.repository.photo;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.everybuy.database.entity.Advertisement;
import ua.everybuy.database.entity.AdvertisementPhoto;
import java.util.List;

@Repository
public interface AdvertisementPhotoRepository extends JpaRepository<AdvertisementPhoto, Long> {
    @EntityGraph (attributePaths = {"advertisement"})
    List<AdvertisementPhoto> findByAdvertisement(Advertisement advertisement);

}
