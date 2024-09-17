package ua.everybuy.buisnesslogic.service.location;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.everybuy.database.entity.Region;
import ua.everybuy.database.repository.RegionRepository;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RegionService {
    private final RegionRepository regionRepository;

    public List<Region> getAllRegions() {
        return regionRepository.findAll();
    }

    public Region findById(Long id) {
        return regionRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Region not found"));
    }
}
