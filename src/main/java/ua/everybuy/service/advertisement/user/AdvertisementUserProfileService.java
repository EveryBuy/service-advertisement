package ua.everybuy.service.advertisement.user;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ua.everybuy.service.category.CategoryService;
import ua.everybuy.service.advertisement.filter.sort.SortStrategyFactory;
import ua.everybuy.database.entity.Advertisement;
import ua.everybuy.database.repository.advertisement.AdvertisementRepository;
import ua.everybuy.database.repository.advertisement.spec.factory.AdvertisementUserFilterSpecificationFactory;
import ua.everybuy.database.repository.advertisement.spec.factory.AdvertisementUserSpecificationFactory;
import ua.everybuy.routing.mapper.AdvertisementUserDtoBuilder;
import ua.everybuy.routing.dto.CategoryAdvertisementCount;
import ua.everybuy.routing.dto.UserAdvertisementDto;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdvertisementUserProfileService {
    private final AdvertisementUserDtoBuilder userDtoBuilder;
    private final AdvertisementUserFilterSpecificationFactory advertisementUserSpecificationFactory;
    private final AdvertisementUserSpecificationFactory advertisementSpecificationFactory;
    private final AdvertisementRepository advertisementRepository;
    private final CategoryService categoryService;
    private final SortStrategyFactory sortStrategyFactory;

    public UserAdvertisementDto getUserActiveFilteredAdvertisements(Long userId, Long categoryId,
                                                                    Advertisement.AdSection section,
                                                                    int page, int size) {

        Page<Advertisement> advertisementsPage = findFilteredAdvertisements(
                userId, categoryId, section, page, size);

        return userDtoBuilder.buildUserAdvertisementDto(userId,
                totalUserAdvertisements(userId),
                advertisementsPage,
                getUserCategoryCounts(userId, section));
    }

    private Page<Advertisement> findFilteredAdvertisements(Long userId, Long categoryId,
                                                           Advertisement.AdSection section,
                                                           int page, int size) {
        checkCategoryExists(categoryId);

        Pageable pageable = createPageable(page, size);
        Specification<Advertisement> spec = advertisementUserSpecificationFactory
                .createSpecification(userId, categoryId, section);

        return advertisementRepository.findAll(spec, pageable);
    }

    private List<CategoryAdvertisementCount> getUserCategoryCounts(Long userId, Advertisement.AdSection section) {
        return advertisementRepository.findCategoryCountsByUserIdAndSection(userId, section);
    }

    private void checkCategoryExists(Long categoryId) {
        if (categoryId != null) {
            categoryService.findById(categoryId);
        }
    }

    private Pageable createPageable(int page, int size) {
        Sort sort = sortStrategyFactory.getSortStrategy(SortStrategyFactory.DATE_DESCENDING).getSortOrder();
        return PageRequest.of(page - 1, size, sort);
    }

    private long totalUserAdvertisements(Long userId) {
        Specification<Advertisement> spec = advertisementSpecificationFactory.createSpecification(userId);
        return advertisementRepository.count(spec);
    }
}
