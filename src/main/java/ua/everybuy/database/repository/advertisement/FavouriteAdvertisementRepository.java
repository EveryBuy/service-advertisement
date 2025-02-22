package ua.everybuy.database.repository.advertisement;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ua.everybuy.database.entity.Advertisement;
import ua.everybuy.database.entity.FavouriteAdvertisement;
import java.util.List;
import java.util.Optional;

@Repository
public interface FavouriteAdvertisementRepository extends JpaRepository<FavouriteAdvertisement, Long>,
        JpaSpecificationExecutor<FavouriteAdvertisement> {
    List<FavouriteAdvertisement> findAllByUserIdOrderByCreatedAtDesc(Long userId);

    Optional<FavouriteAdvertisement> findByUserIdAndAdvertisement(long userId, Advertisement advertisement);

    Boolean existsByUserIdAndAdvertisement(Long userId, Advertisement advertisement);

}
