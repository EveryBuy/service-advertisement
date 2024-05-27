package ua.everybuy.buisnesslogic.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ua.everybuy.database.entity.Advertisement;
import ua.everybuy.database.entity.FavouriteAdvertisement;
import ua.everybuy.database.repository.FavouriteAdvertisementRepository;
import ua.everybuy.routing.dto.response.FavouriteAdvertisementResponse;
import ua.everybuy.routing.dto.response.StatusResponse;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FavouriteAdvertisementService {
    private final FavouriteAdvertisementRepository favouriteAdvertisementRepository;
    private final AdvertisementService advertisementService;

    public List<Advertisement> findAllUserFavouriteAdvertisements(String userId) {
        return favouriteAdvertisementRepository
                .findByUserId(Long.parseLong(userId))
                .stream()
                .map(FavouriteAdvertisement::getAdvertisement)
                .toList();
    }

    public StatusResponse addToFavorites(String userId, Long adId) {
        FavouriteAdvertisement favouriteAdvertisement = new FavouriteAdvertisement();
        favouriteAdvertisement.setUserId(Long.parseLong(userId));
        favouriteAdvertisement.setAdvertisement(advertisementService.findById(adId));
        favouriteAdvertisementRepository.save(favouriteAdvertisement);

        return StatusResponse.builder()
                .status(HttpStatus.CREATED.value())
                .data(mapToFavouriteAdvertisementResponse(favouriteAdvertisement))
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

    private FavouriteAdvertisementResponse mapToFavouriteAdvertisementResponse(FavouriteAdvertisement favouriteAdvertisement) {
        return new FavouriteAdvertisementResponse(favouriteAdvertisement.getId(),
                favouriteAdvertisement.getUserId(),
                favouriteAdvertisement.getAdvertisement().getId());
    }

}
