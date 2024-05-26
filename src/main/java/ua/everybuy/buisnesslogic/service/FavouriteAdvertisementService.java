package ua.everybuy.buisnesslogic.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.everybuy.database.entity.FavouriteAdvertisement;
import ua.everybuy.database.repository.FavouriteAdvertisementRepository;
import ua.everybuy.routing.dto.StatusResponse;

@Service
@RequiredArgsConstructor
public class FavouriteAdvertisementService {
    private final FavouriteAdvertisementRepository favouriteAdvertisementRepository;
    private final AdvertisementService advertisementService;
    public StatusResponse addAdvToFavourite(String userId, long advId){
        FavouriteAdvertisement advertisement = new FavouriteAdvertisement();
        advertisement.setUserId(Long.parseLong(userId));
        advertisement.setAdvertisement(advertisementService.findById(advId));
        favouriteAdvertisementRepository.save(advertisement);
        return StatusResponse.builder()
                .status(200)
                .data("Dodelat`")
                .build();
    }

    public StatusResponse removeAdvertisement(String userId, long advId){
        favouriteAdvertisementRepository.delete(findFavouriteByUserAndAdvertisement(userId, advId));
        return StatusResponse.builder()
                .status(200)
                .data("deleted success")
                .build();
    }

    private FavouriteAdvertisement findFavouriteByUserAndAdvertisement(String userId, long advId){
        return favouriteAdvertisementRepository.findByUserIdAndAdvertisement(
                Long.parseLong(userId), advertisementService.findById(advId)).
                orElseThrow(() ->
                        new EntityNotFoundException("Favourite advertisement with id " + advId + " and user id " + userId + " not found"));
    }
}
