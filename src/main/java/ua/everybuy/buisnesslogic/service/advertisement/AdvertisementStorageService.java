package ua.everybuy.buisnesslogic.service.advertisement;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ua.everybuy.buisnesslogic.service.advertisement.search.AdvertisementIndexService;
import ua.everybuy.database.entity.Advertisement;
import ua.everybuy.database.repository.advertisement.AdvertisementRepository;
import ua.everybuy.errorhandling.message.AdvertisementValidationMessages;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdvertisementStorageService {
    private final AdvertisementRepository advertisementRepository;
    private final AdvertisementIndexService advertisementIndexService;

    public Advertisement save(Advertisement advertisement) {
        advertisementIndexService.indexAdvertisement(advertisement);
        return advertisementRepository.save(advertisement);
    }

    public Advertisement findById(Long id) {
        return advertisementRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(AdvertisementValidationMessages
                        .ADVERTISEMENT_NOT_FOUND_MESSAGE));
    }

    public Advertisement findActiveById(Long id) {
        return advertisementRepository.findActiveById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        AdvertisementValidationMessages.ACTIVE_ADVERTISEMENT_NOT_FOUND_MESSAGE
                ));
    }

    public List<Advertisement> findByUserId(Long userId, boolean isEnabled, Advertisement.AdSection section,
                                            Pageable pageable) {
        return advertisementRepository.findByUserId(userId, isEnabled, section, pageable);
    }

    public Advertisement findAdvertisementByIdAndUserId(Long advertisementId, Long userId) {
        return advertisementRepository.findByIdAndUserId(advertisementId, userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format(AdvertisementValidationMessages
                                .ACCESS_DENIED_MESSAGE_TEMPLATE, userId, advertisementId)));
    }

    public void delete(Advertisement advertisement) {
        advertisementIndexService.deleteFromIndex(advertisement.getId());
        advertisementRepository.delete(advertisement);
    }
}
