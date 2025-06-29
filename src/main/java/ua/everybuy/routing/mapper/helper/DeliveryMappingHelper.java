package ua.everybuy.routing.mapper.helper;

import lombok.RequiredArgsConstructor;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;
import ua.everybuy.service.delivery.DeliveryService;
import ua.everybuy.database.entity.Advertisement;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class DeliveryMappingHelper {
    private final DeliveryService deliveryService;

    @Named("getDeliveryMethods")
    public Set<String> getDeliveryMethods(Advertisement advertisement) {
        return deliveryService.getAdvertisementDeliveryMethods(advertisement);
    }
}
