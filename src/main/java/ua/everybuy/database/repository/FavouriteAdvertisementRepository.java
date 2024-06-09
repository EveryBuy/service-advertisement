package ua.everybuy.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.everybuy.database.entity.Advertisement;
import ua.everybuy.database.entity.FavouriteAdvertisement;

import java.util.List;
import java.util.Optional;

public interface FavouriteAdvertisementRepository extends JpaRepository<FavouriteAdvertisement, Long> {
    List<FavouriteAdvertisement> findByUserId(long userId);

    Optional<FavouriteAdvertisement> findByUserIdAndAdvertisement(long userId, Advertisement advertisement);
    Boolean existsByUserIdAndAdvertisement(Long userId, Advertisement advertisement);

}
