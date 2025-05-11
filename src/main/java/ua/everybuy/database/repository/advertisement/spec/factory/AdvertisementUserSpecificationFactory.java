package ua.everybuy.database.repository.advertisement.spec.factory;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import ua.everybuy.database.entity.Advertisement;
import ua.everybuy.database.repository.advertisement.spec.AdvertisementSearchSpecifications;

@Component
public class AdvertisementUserSpecificationFactory {
    public Specification<Advertisement> createSpecification(Long userId) {
        return Specification
                .where(AdvertisementSearchSpecifications.isEnabled())
                .and(AdvertisementSearchSpecifications.belongsToUser(userId))
                .and(AdvertisementSearchSpecifications.fetchAllRelations());
    }
}
