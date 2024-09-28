package ua.everybuy.buisnesslogic.service.advertisement;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ua.everybuy.buisnesslogic.service.category.CategoryService;
import ua.everybuy.database.entity.Advertisement;
import ua.everybuy.database.entity.FavouriteAdvertisement;
import ua.everybuy.database.repository.FavouriteAdvertisementRepository;
import ua.everybuy.errorhandling.custom.DuplicateDataException;
import ua.everybuy.errorhandling.message.FavouriteAdvertisementValidationMessages;
import ua.everybuy.routing.dto.mapper.FavouriteAdvertisementMapper;
import ua.everybuy.routing.dto.response.AddToFavouriteResponse;
import ua.everybuy.routing.dto.response.FavouriteAdvertisementResponse;
import ua.everybuy.routing.dto.response.StatusResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavouriteAdvertisementService {
    private final FavouriteAdvertisementRepository favouriteAdvertisementRepository;
    private final FavouriteAdvertisementMapper favouriteAdvertisementMapper;
    private final AdvertisementManagementService advertisementManagementService;
    private final StatisticsService statisticsService;
    private final CategoryService categoryService;

    public List<Advertisement> findAllUserFavouriteAdvertisements(String userId) {
        List<FavouriteAdvertisement> favouriteAdvertisements = favouriteAdvertisementRepository
                .findByUserId(Long.parseLong(userId));

        return favouriteAdvertisements.stream()
                .map(FavouriteAdvertisement::getAdvertisement)
                .collect(Collectors.toList());
    }

    public List<Advertisement> filterAdvertisementsByCategory(List<Advertisement> advertisements, Long categoryId) {
        if (categoryId != null) {
            categoryService.findById(categoryId);
            advertisements = advertisements.stream()
                    .filter(advertisement -> advertisement != null &&
                            categoryId.equals(advertisement.getTopSubCategory().getCategory().getId()))
                    .collect(Collectors.toList());
        }
        return advertisements;
    }

    public List<FavouriteAdvertisementResponse> findAllUserFavouriteAdvertisementsWithCategory(String userId, Long categoryId) {
        List<Advertisement> userAdvertisements = findAllUserFavouriteAdvertisements(userId);
        List<Advertisement> filteredAdvertisements = filterAdvertisementsByCategory(userAdvertisements, categoryId);

        return filteredAdvertisements.stream()
                .map(favouriteAdvertisementMapper::mapToFavouriteAdvertisementResponse)
                .collect(Collectors.toList());
    }

    public StatusResponse<AddToFavouriteResponse> addToFavorites(String userId, Long adId) {
        Long userIdLong = Long.parseLong(userId);
        Advertisement advertisement = advertisementManagementService.findById(adId);

        if (favouriteAdvertisementRepository.existsByUserIdAndAdvertisement(userIdLong, advertisement)) {
            throw new DuplicateDataException(FavouriteAdvertisementValidationMessages
                    .DUPLICATE_FAVOURITE_MESSAGE);
        }

        FavouriteAdvertisement newFavouriteAdvertisement = favouriteAdvertisementMapper
                .mapToFavouriteAdvertisementEntity(userIdLong, advertisement);

        newFavouriteAdvertisement = favouriteAdvertisementRepository.save(newFavouriteAdvertisement);
        statisticsService.incrementFavouriteCountAndSave(advertisement);
        AddToFavouriteResponse addToFavouriteResponse = favouriteAdvertisementMapper.mapToAddToFavouriteResponse(newFavouriteAdvertisement);

        return new StatusResponse<>(HttpStatus.CREATED.value(), addToFavouriteResponse);
    }

    public void removeFromFavorites(String userId, Long adId) {
        FavouriteAdvertisement favouriteAdvertisement = findFavouriteByUserAndAdvertisement(userId, adId);
        favouriteAdvertisementRepository.delete(favouriteAdvertisement);
    }

    private FavouriteAdvertisement findFavouriteByUserAndAdvertisement(String userId, Long adId) {
        return favouriteAdvertisementRepository.findByUserIdAndAdvertisement(
                        Long.parseLong(userId),
                        advertisementManagementService.findById(adId))
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format(FavouriteAdvertisementValidationMessages
                                .FAVOURITE_NOT_FOUND_MESSAGE_TEMPLATE, adId, userId)));
    }
}


