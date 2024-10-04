package ua.everybuy.database.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.everybuy.database.entity.Advertisement;
import java.util.List;
import java.util.Optional;

@Repository
public interface AdvertisementRepository extends JpaRepository<Advertisement, Long> {
    Optional <Advertisement> findByIdAndUserId(Long id, Long userId);
    List<Advertisement> findByUserId(Long userId);
    Page<Advertisement> findByIsEnabledTrueOrderByCreationDateDesc(Pageable pageable);
}
