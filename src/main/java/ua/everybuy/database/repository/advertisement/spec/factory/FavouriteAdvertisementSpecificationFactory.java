package ua.everybuy.database.repository.advertisement.spec.factory;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import ua.everybuy.database.entity.Advertisement;
import ua.everybuy.database.entity.FavouriteAdvertisement;
import ua.everybuy.database.repository.advertisement.spec.FavouriteAdvertisementSpecifications;

@Component
public class FavouriteAdvertisementSpecificationFactory {
    public Specification<FavouriteAdvertisement> createSpecification(Long userId, Long categoryId,
                                                                     Advertisement.AdSection adSection) {
        return Specification
                .where(FavouriteAdvertisementSpecifications.userId(userId))
                .and(FavouriteAdvertisementSpecifications.isActiveAdvertisement())
                .and(FavouriteAdvertisementSpecifications.categoryId(categoryId))
                .and(FavouriteAdvertisementSpecifications.adSection(adSection));
    }
}
