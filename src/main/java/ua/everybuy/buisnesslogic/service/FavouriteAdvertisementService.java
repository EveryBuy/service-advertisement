package ua.everybuy.buisnesslogic.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ua.everybuy.database.entity.Advertisement;
import ua.everybuy.database.entity.FavouriteAdvertisement;
import ua.everybuy.database.repository.FavouriteAdvertisementRepository;
import ua.everybuy.errorhandling.custom.DuplicateDataException;
import ua.everybuy.routing.dto.mapper.AdvertisementMapper;
import ua.everybuy.routing.dto.response.FavouriteAdvertisementResponse;
import ua.everybuy.routing.dto.response.StatusResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavouriteAdvertisementService {
    private final FavouriteAdvertisementRepository favouriteAdvertisementRepository;
    private final AdvertisementMapper mapper;
    private final AdvertisementService advertisementService;
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
                    .filter(advertisement ->
                            categoryId.equals(advertisement.getSubCategory().getCategory().getId()))
                    .collect(Collectors.toList());
        }

        return advertisements;
    }
    public List<FavouriteAdvertisementResponse> findAllUserFavouriteAdvertisementsWithCategory(String userId, Long categoryId) {
        List<Advertisement> userAdvertisements = findAllUserFavouriteAdvertisements(userId);

        List<Advertisement> filteredAdvertisements = filterAdvertisementsByCategory(userAdvertisements, categoryId);

        return filteredAdvertisements.stream()
                .map(mapper::mapToFavouriteAdvertisementResponse)
                .collect(Collectors.toList());
    }

    public StatusResponse addToFavorites(String userId, Long adId) {
        Long userIdLong = Long.parseLong(userId);
        Advertisement advertisement = advertisementService.findById(adId);

        if (favouriteAdvertisementRepository.existsByUserIdAndAdvertisement(userIdLong, advertisement)) {
            throw new DuplicateDataException("Advertisement is already added to favorites");
        }

        FavouriteAdvertisement favouriteAdvertisement = new FavouriteAdvertisement();
        favouriteAdvertisement.setUserId(userIdLong);
        favouriteAdvertisement.setAdvertisement(advertisement);
        favouriteAdvertisementRepository.save(favouriteAdvertisement);

        return StatusResponse.builder()
                .status(HttpStatus.CREATED.value())
                .data(mapper.mapToAddToFavouriteResponse(favouriteAdvertisement))
                .build();
    }

    public void removeFromFavorites(String userId, Long adId) {
        FavouriteAdvertisement favouriteAdvertisement = findFavouriteByUserAndAdvertisement(userId, adId);
        favouriteAdvertisementRepository.delete(favouriteAdvertisement);
    }

    private FavouriteAdvertisement findFavouriteByUserAndAdvertisement(String userId, Long adId) {
        return favouriteAdvertisementRepository.findByUserIdAndAdvertisement(
                        Long.parseLong(userId),
                        advertisementService.findById(adId))
                .orElseThrow(() -> new EntityNotFoundException(
                        "Favourite advertisement with id " + adId + " and user id " + userId + " not found"));
    }

}
