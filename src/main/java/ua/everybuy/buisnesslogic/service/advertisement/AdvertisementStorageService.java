package ua.everybuy.buisnesslogic.service.advertisement;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.everybuy.database.entity.Advertisement;
import ua.everybuy.database.repository.AdvertisementRepository;
import ua.everybuy.errorhandling.message.AdvertisementValidationMessages;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdvertisementStorageService {
    private final AdvertisementRepository advertisementRepository;

    public Advertisement save(Advertisement advertisement) {
        return advertisementRepository.save(advertisement);
    }

    public Advertisement findById(Long id) {
        return advertisementRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(AdvertisementValidationMessages
                        .ADVERTISEMENT_NOT_FOUND_MESSAGE));
    }
    public List<Advertisement> findByUserId(Long userId) {
        return advertisementRepository.findByUserId(userId);
    }

    public List<Advertisement> getActiveAdvertisements() {
        return advertisementRepository.findByIsEnabledTrueOrderByCreationDateDesc();
    }

    public Advertisement findAdvertisementByIdAndUserId(Long advertisementId, Long userId) {
        return advertisementRepository.findByIdAndUserId(advertisementId, userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format(AdvertisementValidationMessages
                                .ACCESS_DENIED_MESSAGE_TEMPLATE, userId, advertisementId)));
    }

    public void delete(Advertisement advertisement) {
        advertisementRepository.delete(advertisement);
    }
}
