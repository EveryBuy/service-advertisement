package ua.everybuy.buisnesslogic.service.advertisement.filter;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ua.everybuy.buisnesslogic.strategy.sort.SortStrategyFactory;
import ua.everybuy.database.entity.Advertisement;
import ua.everybuy.database.repository.advertisement.AdvertisementRepository;
import ua.everybuy.database.repository.advertisement.spec.factory.AdvertisementSearchSpecificationFactory;
import ua.everybuy.routing.dto.PriceRangeDto;
import ua.everybuy.routing.dto.request.AdvertisementSearchParametersDto;

import static ua.everybuy.buisnesslogic.strategy.sort.SortStrategyFactory.PRICE_ASCENDING;
import static ua.everybuy.buisnesslogic.strategy.sort.SortStrategyFactory.PRICE_DESCENDING;

@Service
@RequiredArgsConstructor
public class FilterPriceRangeService {

    private final AdvertisementRepository advertisementRepository;
    private final AdvertisementSearchSpecificationFactory filterAdSpecFactory;
    private final SortStrategyFactory sortStrategyFactory;

    public PriceRangeDto getPriceRange(AdvertisementSearchParametersDto searchParametersDto) {
        Double minPrice = getPrice(searchParametersDto, PRICE_ASCENDING);
        Double maxPrice = getPrice(searchParametersDto, PRICE_DESCENDING);
        return new PriceRangeDto(minPrice, maxPrice);
    }

    private Double getPrice(AdvertisementSearchParametersDto searchParametersDto, String priceOrder) {
        Sort priceSort = sortStrategyFactory.getSortStrategy(priceOrder).getSortOrder();
        Pageable pageRequest = PageRequest.of(0, 1, priceSort);
        Page<Advertisement> adPage = findAdvertisements(searchParametersDto, pageRequest);
        return adPage.hasContent() ? adPage.getContent().get(0).getPrice() : 0.0;
    }

    private Page<Advertisement> findAdvertisements(AdvertisementSearchParametersDto searchParametersDto, Pageable pageRequest) {
        return advertisementRepository.findAll(
                filterAdSpecFactory.createSpecification(searchParametersDto), pageRequest);
    }
}

