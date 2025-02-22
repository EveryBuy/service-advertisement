package ua.everybuy.buisnesslogic.service.location;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ua.everybuy.database.entity.Region;
import ua.everybuy.database.repository.location.RegionRepository;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RegionService {
    private final RegionRepository regionRepository;

    @Cacheable(value = "regionsCache")
    public List<Region> getAllRegions() {
        log.info("[CACHE INFO] Fetching all regions - Cache START");
        return regionRepository.findAll();
    }

    public Region findById(Long id) {
        return regionRepository.findById(id).orElseThrow(()
                -> new EntityNotFoundException("Region not found"));
    }
}
