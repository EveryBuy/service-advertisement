package ua.everybuy.buisnesslogic.service.advertisement.filter;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import ua.everybuy.database.entity.Advertisement;

@Component
public class FilterAdvertisementSpecificationFactory {
    private static final double SIMILARITY_THRESHOLD = 0.5;

    public Specification<Advertisement> createSpecification(Double minPrice, Double maxPrice,
                                                            Long regionId, Long cityId,
                                                            Long topSubCategoryId, Long lowSubCategoryId, Long categoryId,
                                                            Advertisement.ProductType productType,
                                                            Advertisement.AdSection section, String keyword) {
        return Specification
                .where(AdvertisementSpecifications.isEnabled())
                .and(AdvertisementSpecifications.fetchCityAndRegion())
                .and(AdvertisementSpecifications.hasMinPrice(minPrice))
                .and(AdvertisementSpecifications.hasMaxPrice(maxPrice))
                .and(AdvertisementSpecifications.belongsToCity(cityId))
                .and(AdvertisementSpecifications.belongsToRegion(regionId))
                .and(AdvertisementSpecifications.belongsToTopSubCategory(topSubCategoryId))
                .and(AdvertisementSpecifications.belongsToLowSubCategory(lowSubCategoryId))
                .and(AdvertisementSpecifications.belongsToCategory(categoryId))
                .and(AdvertisementSpecifications.hasProductType(productType))
                .and(AdvertisementSpecifications.hasSection(section))
                .and(AdvertisementSpecifications.hasSimilarTitle(keyword, SIMILARITY_THRESHOLD));
    }
}
