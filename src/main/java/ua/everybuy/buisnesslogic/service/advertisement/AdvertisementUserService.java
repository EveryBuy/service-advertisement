package ua.everybuy.buisnesslogic.service.advertisement;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ua.everybuy.buisnesslogic.service.category.CategoryService;
import ua.everybuy.buisnesslogic.strategy.sort.SortStrategyFactory;
import ua.everybuy.database.entity.Advertisement;
import ua.everybuy.database.repository.advertisement.AdvertisementRepository;
import ua.everybuy.database.repository.advertisement.spec.factory.AdvertisementUserSpecificationFactory;
import ua.everybuy.routing.AdvertisementUserDtoBuilder;
import ua.everybuy.routing.dto.CategoryAdvertisementCount;
import ua.everybuy.routing.dto.UserAdvertisementDto;
import ua.everybuy.routing.mapper.AdvertisementResponseMapper;
import ua.everybuy.routing.dto.response.AdvertisementWithStatisticResponse;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdvertisementUserService {

    private final AdvertisementStorageService advertisementStorageService;
    private final AdvertisementResponseMapper advertisementResponseMapper;
    private final AdvertisementUserDtoBuilder userDtoBuilder;
    private final AdvertisementUserSpecificationFactory advertisementUserSpecificationFactory;
    private final SortStrategyFactory sortStrategyFactory;
    private final AdvertisementRepository advertisementRepository;
    private final CategoryService categoryService;
    private final AdvertisementUserDeletionService advertisementUserDeletionService;

    public List<AdvertisementWithStatisticResponse> getUserAdvertisements(Long userId, boolean isEnabled,
                                                                          Advertisement.AdSection section,
                                                                          int page, int size) {
        List<Advertisement> advertisements = findUserAdvertisements(userId, isEnabled, section, page, size);

        return advertisements.stream()
                .map(advertisementResponseMapper::mapToAdvertisementStatisticResponse)
                .toList();
    }

    private List<Advertisement> findUserAdvertisements(Long userId, Boolean isEnabled,
                                                       Advertisement.AdSection section,
                                                       int page, int size) {
        Pageable pageable = createPageable(page, size);

        return advertisementStorageService.findByUserId(userId, isEnabled, section, pageable);
    }

    public UserAdvertisementDto getUserActiveAdvertisements(Long userId, Long categoryId,
                                                            int page, int size) {

        Page<Advertisement> advertisementsPage = findFilteredAdvertisements(
                userId, categoryId, page, size);

        return userDtoBuilder.buildUserAdvertisementDto(userId, advertisementsPage, getUserCategoryCounts(userId));
    }

    private Page<Advertisement> findFilteredAdvertisements(Long userId, Long categoryId,
                                                           int page, int size) {
        validCategory(categoryId);

        Pageable pageable = createPageable(page, size);

        Specification<Advertisement> spec = advertisementUserSpecificationFactory
                .createSpecification(userId, categoryId);

        return advertisementRepository.findAll(spec, pageable);
    }

    public List<CategoryAdvertisementCount> getUserCategoryCounts(Long userId) {
        return advertisementRepository.findCategoryCountsByUserId(userId);
    }

    public void deleteAllAndPushUserAdvertisements(Long userId, HttpServletRequest request) throws IOException {
        advertisementUserDeletionService.deleteAllUserAdvertisements(userId, request);
    }

    private Pageable createPageable(int page, int size) {
        Sort sort = sortStrategyFactory.getSortStrategy(SortStrategyFactory.DATE_DESCENDING).getSortOrder();
        return PageRequest.of(page - 1, size, sort);
    }

    private void validCategory(Long categoryId) {
        if (categoryId != null) {
            categoryService.findById(categoryId);
        }

    }

}
