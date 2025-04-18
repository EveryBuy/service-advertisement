package ua.everybuy.database.repository.advertisement.spec.factory;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import ua.everybuy.database.entity.Advertisement;
import ua.everybuy.database.repository.advertisement.spec.AdvertisementSearchSpecifications;

@Component
public class AdvertisementDistinctTopCategorySpecificationFactory {
    private static final double SIMILARITY_THRESHOLD = 0.3;

    public Specification<Advertisement> createSpecification(String keyword) {
        return Specification
                .where(AdvertisementSearchSpecifications.isEnabled())
                .and(AdvertisementSearchSpecifications.fetchCityAndRegion())
                .and(AdvertisementSearchSpecifications.hasSimilarTitle(keyword, SIMILARITY_THRESHOLD))
                .and(AdvertisementSearchSpecifications.distinctByCategory());
    }
}
