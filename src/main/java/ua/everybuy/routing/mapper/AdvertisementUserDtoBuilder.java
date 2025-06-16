package ua.everybuy.routing.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import ua.everybuy.service.integration.UserProfileService;
import ua.everybuy.database.entity.Advertisement;
import ua.everybuy.routing.dto.CategoryAdvertisementCount;
import ua.everybuy.routing.dto.CategoryAdvertisementCountDto;
import ua.everybuy.routing.dto.UserAdvertisementDto;
import ua.everybuy.routing.dto.response.FilteredAdvertisementsResponse;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AdvertisementUserDtoBuilder {
    private final UserProfileService userService;
    private final AdvertisementFilterMapper advertisementFilterMapper;
    private final SubCategoryMapper subCategoryMapper;

    public UserAdvertisementDto buildUserAdvertisementDto(Long userId,
                                                          long totalAdvertisements,
                                                          Page<Advertisement> advertisementsPage,
                                                          List<CategoryAdvertisementCount> categories) {
        return UserAdvertisementDto.builder()
                .user(userService.getShortUserInfo(userId))
                .totalAdvertisements(totalAdvertisements)
                .totalFilteredAdvertisements(advertisementsPage.getTotalElements())
                .totalPages(advertisementsPage.getTotalPages())
                .categories(mapToCategoryDto(categories))
                .filteredAds(mapToFilteredResponses(advertisementsPage.getContent()))
                .build();
    }

    private List<FilteredAdvertisementsResponse> mapToFilteredResponses(List<Advertisement> advertisements) {
        return advertisements.stream()
                .map(advertisementFilterMapper::mapToFilteredAdvertisementsResponse)
                .toList();
    }

    private List<CategoryAdvertisementCountDto> mapToCategoryDto(List<CategoryAdvertisementCount> categories) {
        return categories.stream()
                .map(subCategoryMapper::mapToCategoryAdvertisementCountDto)
                .toList();
    }
}
