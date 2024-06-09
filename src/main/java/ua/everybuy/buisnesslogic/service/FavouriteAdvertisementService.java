package ua.everybuy.buisnesslogic.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ua.everybuy.database.entity.Advertisement;
import ua.everybuy.database.entity.FavouriteAdvertisement;
import ua.everybuy.database.repository.FavouriteAdvertisementRepository;
import ua.everybuy.routing.dto.mapper.FavouriteAdvertisementMapper;
import ua.everybuy.routing.dto.response.ShortAdvertisementResponse;
import ua.everybuy.routing.dto.response.StatusResponse;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FavouriteAdvertisementService {
    private final FavouriteAdvertisementRepository favouriteAdvertisementRepository;
    private final FavouriteAdvertisementMapper mapper = FavouriteAdvertisementMapper.INSTANCE;
    private final AdvertisementService advertisementService;

    public List<ShortAdvertisementResponse> findAllUserFavouriteAdvertisements(String userId) {
        return favouriteAdvertisementRepository
                .findByUserId(Long.parseLong(userId))
                .stream()
                .map(favouriteAdvertisement -> favouriteAdvertisement.getAdvertisement())
                .map(mapper::toShortAdvertisementResponse)
                .toList();
    }

    public StatusResponse addToFavorites(String userId, Long adId) {
        Long userIdLong = Long.parseLong(userId);
        Advertisement advertisement = advertisementService.findById(adId);

        if (favouriteAdvertisementRepository.existsByUserIdAndAdvertisement(userIdLong, advertisement)) {
            throw new IllegalArgumentException("Advertisement is already added to favorites");
        }

        FavouriteAdvertisement favouriteAdvertisement = new FavouriteAdvertisement();
        favouriteAdvertisement.setUserId(userIdLong);
        favouriteAdvertisement.setAdvertisement(advertisement);
        favouriteAdvertisementRepository.save(favouriteAdvertisement);

        return StatusResponse.builder()
                .status(HttpStatus.CREATED.value())
                .data(mapper.toFavouriteAdvertisementResponse(favouriteAdvertisement))
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
