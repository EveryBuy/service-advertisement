package ua.everybuy.buisnesslogic.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.everybuy.database.entity.Advertisement;
import ua.everybuy.database.entity.FavouriteAdvertisement;
import ua.everybuy.database.repository.FavouriteAdvertisementRepository;
import ua.everybuy.routing.dto.FavouriteAdvertisementResponse;
import ua.everybuy.routing.dto.StatusResponse;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FavouriteAdvertisementService {
    private final FavouriteAdvertisementRepository favouriteAdvertisementRepository;
    private final AdvertisementService advertisementService;

    public List<Advertisement> findAllUsersFavouriteAdvertisements(String userId){
        return favouriteAdvertisementRepository.findByUserId(Long.parseLong(userId))
                .stream()
                .map(FavouriteAdvertisement::getAdvertisement)
                .toList();
    }
    public StatusResponse addAdvToFavourite(String userId, long advId){
        FavouriteAdvertisement favouriteAdvertisement = new FavouriteAdvertisement();
        favouriteAdvertisement.setUserId(Long.parseLong(userId));
        favouriteAdvertisement.setAdvertisement(advertisementService.findById(advId));
        favouriteAdvertisementRepository.save(favouriteAdvertisement);
        return StatusResponse.builder()
                .status(201)
                .data(convertAdvToFavAdvResponse(favouriteAdvertisement))
                .build();
    }

    public StatusResponse removeAdvertisement(String userId, long advId){
        FavouriteAdvertisement favouriteAdvertisement = findFavouriteByUserAndAdvertisement(userId, advId);
        favouriteAdvertisementRepository.delete(favouriteAdvertisement);

        return StatusResponse.builder()
                .status(204)
                .data(favouriteAdvertisement)
                .build();
    }

    private FavouriteAdvertisement findFavouriteByUserAndAdvertisement(String userId, long advId){
        return favouriteAdvertisementRepository.findByUserIdAndAdvertisement(
                Long.parseLong(userId), advertisementService.findById(advId)).
                orElseThrow(() ->
                        new EntityNotFoundException("Favourite advertisement with id " + advId + " and user id " + userId + " not found"));
    }

    private FavouriteAdvertisementResponse convertAdvToFavAdvResponse(FavouriteAdvertisement favouriteAdvertisement){
        return new FavouriteAdvertisementResponse(favouriteAdvertisement.getId(),
                favouriteAdvertisement.getUserId(),
                favouriteAdvertisement.getAdvertisement().getId());
    }


}
