package ua.everybuy.buisnesslogic.service.advertisement;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
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

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdvertisementFavouriteService {
    private final FavouriteAdvertisementRepository favouriteAdvertisementRepository;
    private final FavouriteAdvertisementMapper favouriteAdvertisementMapper;
    private final AdvertisementStorageService advertisementStorageService;
    private final StatisticsService statisticsService;
    private final CategoryService categoryService;

    public List<FavouriteAdvertisementResponse> findUserFavouriteAdvertisementsByCategoryAndSection(Principal principal,
                                                                                                    Long categoryId,
                                                                                                    Advertisement.AdSection adSection) {
        Long userId = Long.parseLong(principal.getName());
        checkCategoryValid(categoryId);
        return findAllUserFavouriteAdvertisements(userId).stream()
                .filter(ad -> categoryId == null || categoryId.equals(ad.getTopSubCategory().getCategory().getId()))
                .filter(ad -> adSection == null || ad.getSection().equals(adSection))
                .map(favouriteAdvertisementMapper::mapToFavouriteAdvertisementResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public StatusResponse<AddToFavouriteResponse> addToFavorites(Principal principal, Long adId) {
        Long userId = Long.parseLong(principal.getName());
        Advertisement advertisement = advertisementStorageService.findById(adId);

        checkDuplicateFavourite(userId, advertisement);

        FavouriteAdvertisement newFavouriteAdvertisement = favouriteAdvertisementMapper
                .mapToFavouriteAdvertisementEntity(userId, advertisement);

        newFavouriteAdvertisement = favouriteAdvertisementRepository.save(newFavouriteAdvertisement);
        statisticsService.incrementFavouriteCountAndSave(advertisement);
        AddToFavouriteResponse addToFavouriteResponse = favouriteAdvertisementMapper.mapToAddToFavouriteResponse(newFavouriteAdvertisement);

        return new StatusResponse<>(HttpStatus.CREATED.value(), addToFavouriteResponse);
    }

    @Transactional
    public void removeFromFavorites(Principal principal, Long adId) {
        Long userId = Long.parseLong(principal.getName());
        FavouriteAdvertisement favouriteAdvertisement = findFavouriteByUserAndAdvertisement(userId, adId);
        favouriteAdvertisementRepository.delete(favouriteAdvertisement);
    }

    private List<Advertisement> findAllUserFavouriteAdvertisements(Long userId) {
        return favouriteAdvertisementRepository.findByUserId(userId).stream()
                .map(FavouriteAdvertisement::getAdvertisement)
                .collect(Collectors.toList());
    }

    private FavouriteAdvertisement findFavouriteByUserAndAdvertisement(Long userId, Long adId) {
        return favouriteAdvertisementRepository.findByUserIdAndAdvertisement(userId,
                advertisementStorageService.findById(adId)).orElseThrow(() ->
                new EntityNotFoundException(String.format(FavouriteAdvertisementValidationMessages
                        .FAVOURITE_NOT_FOUND_MESSAGE_TEMPLATE, adId, userId)));
    }

    private void checkDuplicateFavourite(Long userId, Advertisement advertisement) {
        if (favouriteAdvertisementRepository.existsByUserIdAndAdvertisement(userId, advertisement)) {
            throw new DuplicateDataException(FavouriteAdvertisementValidationMessages
                    .DUPLICATE_FAVOURITE_MESSAGE);
        }
    }

    private void checkCategoryValid(Long categoryId) {
        if (categoryId != null) {
            categoryService.findById(categoryId);
        }

    }
}
