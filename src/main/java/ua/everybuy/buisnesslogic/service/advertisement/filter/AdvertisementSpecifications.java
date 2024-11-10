package ua.everybuy.buisnesslogic.service.advertisement.filter;

import org.springframework.data.jpa.domain.Specification;
import ua.everybuy.database.entity.Advertisement;

public class AdvertisementSpecifications {
    public static Specification<Advertisement> isEnabled() {
        return (root, query, cb) -> cb.isTrue(root.get("isEnabled"));
    }

    public static Specification<Advertisement> hasMinPrice(Double minPrice) {
        return (root, query, cb) -> minPrice == null ? null : cb.greaterThanOrEqualTo(root.get("price"), minPrice);
    }

    public static Specification<Advertisement> hasMaxPrice(Double maxPrice) {
        return (root, query, cb) -> maxPrice == null ? null : cb.lessThanOrEqualTo(root.get("price"), maxPrice);
    }

    public static Specification<Advertisement> belongsToRegion(Long regionId) {
        return (root, query, cb) -> regionId == null ? null : cb.equal(root.get("city").get("region").get("id"), regionId);
    }

    public static Specification<Advertisement> belongsToTopSubCategory(Long topSubCategoryId) {
        return (root, query, cb) -> topSubCategoryId == null ? null : cb.equal(root.get("topSubCategory").get("id"), topSubCategoryId);
    }

    public static Specification<Advertisement> belongsToLowSubCategory(Long lowSubCategoryId) {
        return (root, query, cb) -> lowSubCategoryId == null ? null : cb.equal(root.get("lowSubCategory").get("id"), lowSubCategoryId);
    }

    public static Specification<Advertisement> belongsToCategory(Long categoryId) {
        return (root, query, cb) -> categoryId == null ? null : cb.equal(root.get("topSubCategory").get("category").get("id"), categoryId);
    }

    public static Specification<Advertisement> hasProductType(Advertisement.ProductType productType) {
        return (root, query, cb) -> productType == null ? null : cb.equal(root.get("productType"), productType);
    }

    public static Specification<Advertisement> hasSection(Advertisement.AdSection adSection) {
        return (root, query, cb) -> adSection == null ? null : cb.equal(root.get("section"), adSection);
    }

    public static Specification<Advertisement> hasSimilarTitle(String keyword, double minSimilarity) {
        return (root, query, cb) -> {
            if (keyword == null || keyword.isEmpty()) {
                return null;
            }

            var similarityCondition = cb.greaterThanOrEqualTo(
                    cb.function("similarity", Double.class,
                            cb.function("LOWER", String.class, root.get("title")),
                            cb.function("LOWER", String.class, cb.literal(keyword))
                    ),
                    minSimilarity
            );

            var ilikeCondition = cb.like(
                    cb.lower(root.get("title")),
                    "%" + keyword.toLowerCase() + "%"
            );

            return cb.or(similarityCondition, ilikeCondition);
        };
    }
}
