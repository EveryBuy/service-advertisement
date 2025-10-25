package ua.everybuy.service.advertisement;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import ua.everybuy.service.advertisement.search.AdvertisementIndexingService;
import ua.everybuy.database.entity.Advertisement;
import ua.everybuy.database.repository.advertisement.AdvertisementRepository;
import ua.everybuy.errorhandling.message.AdvertisementValidationMessages;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdvertisementStorageService {
    private final AdvertisementRepository advertisementRepository;
    private final AdvertisementIndexingService advertisementIndexingService;

    @Transactional
    public Advertisement save(Advertisement advertisement) {
        Advertisement savedAdvertisement = advertisementRepository.save(advertisement);

        // Register callback to execute after successful commit
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                advertisementIndexingService.indexAdvertisement(savedAdvertisement);
            }
        });

        return savedAdvertisement;
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
        advertisementIndexingService.deleteFromIndex(advertisement.getId());
        advertisementRepository.delete(advertisement);
    }
}
