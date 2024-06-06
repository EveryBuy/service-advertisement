package ua.everybuy.routing.dto.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ua.everybuy.buisnesslogic.service.CityService;
import ua.everybuy.buisnesslogic.service.SubCategoryService;
import ua.everybuy.database.entity.Advertisement;
import ua.everybuy.routing.dto.AdvertisementDto;
import ua.everybuy.routing.dto.request.CreateAdvertisementRequest;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
@RequiredArgsConstructor
@Component
public class AdvertisementMapper {
    private final CityService cityService;
    private final SubCategoryService subCategoryService;

    public Advertisement mapToEntity(CreateAdvertisementRequest request) {

        return Advertisement.builder()
                .title(request.title())
                .description(request.description())
                .price(request.price())
                .creationDate(LocalDateTime.now())
                .city(cityService.findById(request.cityId()))
                .subCategory(subCategoryService.findById(request.subCategoryId()))
                .productType(request.productType())
                .deliveryMethods(new HashSet<>(request.deliveryMethods()))
                .isEnabled(true)
                .creationDate(LocalDateTime.now())
                .build();
    }

    public AdvertisementDto mapToDto(Advertisement advertisement, List<String> photos) {
        return AdvertisementDto.builder()
                .id(advertisement.getId())
                .title(advertisement.getTitle())
                .description(advertisement.getDescription())
                .price(advertisement.getPrice())
                .mainPhotoUrl(advertisement.getMainPhotoUrl())
                .creationDate(advertisement.getCreationDate())
                .isEnabled(advertisement.getIsEnabled())
                .userId(advertisement.getUserId())
                .cityName(advertisement.getCity())
                .subCategoryName(advertisement.getSubCategory())
                .productType(String.valueOf(advertisement.getProductType()))
                .deliveryMethods(new HashSet<>(advertisement.getDeliveryMethods()))
                .photoUrls(photos)
                .build();
    }
}
