package ua.everybuy.buisnesslogic.service.advertisement.favourite;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ua.everybuy.buisnesslogic.service.advertisement.AdvertisementStorageService;
import ua.everybuy.buisnesslogic.service.advertisement.StatisticsService;
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
public class FavouriteAdvertisementService {
    private final FavouriteAdvertisementRepository favouriteAdvertisementRepository;
    private final FavouriteAdvertisementSpecificationFactory favouriteAdvertisementSpecifications;
    private final FavouriteAdvertisementMapper favouriteAdvertisementMapper;
    private final AdvertisementStorageService advertisementStorageService;
    private final StatisticsService statisticsService;
    private final CategoryService categoryService;

    private Page<FavouriteAdvertisement> findUserFavouriteAdvertisementsByCategoryAndSection(Principal principal, Long categoryId,
                                                                                             Advertisement.AdSection adSection,
                                                                                             int page, int size) {
        Long userId = Long.parseLong(principal.getName());
        if (categoryId != null) {
            checkCategoryValid(categoryId);
        }

        Specification<FavouriteAdvertisement> spec = favouriteAdvertisementSpecifications
                .createSpecification(userId, categoryId, adSection);

        Sort dateSort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(page - 1, size, dateSort);

        return favouriteAdvertisementRepository.findAll(spec, pageable);

    }

    public List<FavouriteAdvertisementResponse> getUserFavouriteAdvertisements(Principal principal, Long categoryId,
                                                                               Advertisement.AdSection adSection,
                                                                               int page, int size) {
        return findUserFavouriteAdvertisementsByCategoryAndSection(principal, categoryId, adSection, page, size).stream()
                .map(fa -> favouriteAdvertisementMapper.mapToFavouriteAdvertisementResponse(fa.getAdvertisement()))
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
