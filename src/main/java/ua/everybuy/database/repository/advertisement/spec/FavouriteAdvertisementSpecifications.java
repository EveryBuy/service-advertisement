package ua.everybuy.database.repository.advertisement.spec;

import org.springframework.data.jpa.domain.Specification;
import ua.everybuy.database.entity.Advertisement;
import ua.everybuy.database.entity.FavouriteAdvertisement;

public class FavouriteAdvertisementSpecifications {

    public static Specification<FavouriteAdvertisement> userId(Long userId) {
        return (root, query, cb) -> cb.equal(root.get("userId"), userId);
    }

    public static Specification<FavouriteAdvertisement> categoryId(Long categoryId) {
        return (root, query, cb) -> categoryId == null
                ? cb.conjunction()
                : cb.equal(root.get("advertisement").get("topSubCategory").get("category").get("id"), categoryId);
    }

    public static Specification<FavouriteAdvertisement> adSection(Advertisement.AdSection adSection) {
        return (root, query, cb) -> adSection == null
                ? cb.conjunction()
                : cb.equal(root.get("advertisement").get("section"), adSection);
    }
    public static Specification<FavouriteAdvertisement> isActiveAdvertisement() {
        return (root, query, cb) -> cb.isTrue(root.get("advertisement").get("isEnabled"));
    }
}
