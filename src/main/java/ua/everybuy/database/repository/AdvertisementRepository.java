package ua.everybuy.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.everybuy.database.entity.Advertisement;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdvertisementRepository extends JpaRepository<Advertisement, Long> {
    List<Advertisement> findByUserId(Long userId);
    Optional <Advertisement> findByIdAndUserId(Long id, Long userId);

}
