package ua.everybuy.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.everybuy.database.entity.Region;
@Repository
public interface RegionRepository extends JpaRepository<Region, Long> {
}

