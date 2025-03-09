package ua.everybuy.database.repository.advertisement.spec.factory;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import ua.everybuy.database.entity.Advertisement;
import ua.everybuy.database.repository.advertisement.spec.AdvertisementSearchSpecifications;
import ua.everybuy.routing.dto.request.AdvertisementSearchParametersDto;

@Component
public class AdvertisementSearchSpecificationFactory {
    private static final double SIMILARITY_THRESHOLD = 0.4;

    public Specification<Advertisement> createSpecification(AdvertisementSearchParametersDto params) {
        return Specification
                .where(AdvertisementSearchSpecifications.isEnabled())
                .and(AdvertisementSearchSpecifications.fetchCityAndRegion())
                .and(AdvertisementSearchSpecifications.hasMinPrice(params.getMinPrice()))
                .and(AdvertisementSearchSpecifications.hasMaxPrice(params.getMaxPrice()))
                .and(AdvertisementSearchSpecifications.belongsToCity(params.getCityId()))
                .and(AdvertisementSearchSpecifications.belongsToRegion(params.getRegionId()))
                .and(AdvertisementSearchSpecifications.belongsToTopSubCategory(params.getTopSubCategoryId()))
                .and(AdvertisementSearchSpecifications.belongsToLowSubCategory(params.getLowSubCategoryId()))
                .and(AdvertisementSearchSpecifications.belongsToCategory(params.getCategoryId()))
                .and(AdvertisementSearchSpecifications.hasProductType(params.getProductType()))
                .and(AdvertisementSearchSpecifications.hasSection(params.getSection()))
                .and(AdvertisementSearchSpecifications.hasSimilarTitle(params.getKeyword(), SIMILARITY_THRESHOLD));
    }
}
