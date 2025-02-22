package ua.everybuy.buisnesslogic.service.advertisement;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.everybuy.buisnesslogic.strategy.delivery.DeliveryMethod;
import ua.everybuy.buisnesslogic.strategy.delivery.DeliveryMethodFactory;
import ua.everybuy.database.entity.Advertisement;
import ua.everybuy.database.entity.AdvertisementDelivery;
import ua.everybuy.database.repository.advertisement.AdvertisementDeliveryRepository;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeliveryService {
    private final DeliveryMethodFactory deliveryMethodFactory;
    private final AdvertisementDeliveryRepository advertisementDeliveryRepository;

    @Transactional
    public void saveAdvertisementDeliveries(Advertisement advertisement, Set<String> deliveryMethodNames) {
        Set<DeliveryMethod> deliveryMethods = getDeliveryMethods(deliveryMethodNames);
        saveDeliveries(advertisement, deliveryMethods);
    }

    @Transactional
    public void updateAdvertisementDeliveries(Advertisement advertisement, Set<String> deliveryMethodNames) {
        deleteExistingDeliveries(advertisement);
        saveAdvertisementDeliveries(advertisement, deliveryMethodNames);
    }

    @Transactional(readOnly = true)
    public Set<String> getAdvertisementDeliveryMethods(Advertisement advertisement) {
        return advertisementDeliveryRepository.findByAdvertisement(advertisement)
                .stream()
                .map(AdvertisementDelivery::getDeliveryMethod)
                .collect(Collectors.toSet());

    }

    private Set<DeliveryMethod> getDeliveryMethods(Set<String> deliveryMethodNames) {
        return deliveryMethodFactory.getDeliveryMethodsFromNames(deliveryMethodNames);
    }

    private void saveDeliveries(Advertisement advertisement, Set<DeliveryMethod> deliveryMethods) {
        for (DeliveryMethod method : deliveryMethods) {
            AdvertisementDelivery advertisementDelivery = createAdvertisementDelivery(advertisement, method);
            advertisementDeliveryRepository.save(advertisementDelivery);
        }
    }

    private AdvertisementDelivery createAdvertisementDelivery(Advertisement advertisement, DeliveryMethod method) {
        return AdvertisementDelivery.builder()
                .advertisement(advertisement)
                .deliveryMethod(method.getName())
                .build();
    }

    private void deleteExistingDeliveries(Advertisement advertisement) {
        advertisementDeliveryRepository.deleteAllByAdvertisement(advertisement);
    }
}
