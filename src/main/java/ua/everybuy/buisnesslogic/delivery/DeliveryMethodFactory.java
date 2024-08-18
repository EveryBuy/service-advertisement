package ua.everybuy.buisnesslogic.delivery;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.everybuy.database.entity.DeliveryMethod;

import java.util.*;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class DeliveryMethodFactory {
    private Map<String, DeliveryMethodHandler> deliveryMethods;

    @PostConstruct
    public void init() {
        deliveryMethods = Map.of(
                "NOVA_POST", new NovaPost(),
                "UKR_POST", new UkrPost(),
                "MEEST_EXPRESS", new MeestExpress()
        );
    }

    public Set<DeliveryMethod> getDeliveryMethodsFromNames(Set<String> names) {
        Set<DeliveryMethod> methods = new HashSet<>();
        for (String name : names) {
            String upperCaseName = name.toUpperCase();
            DeliveryMethodHandler method = deliveryMethods.get(upperCaseName);
            if (method == null) {
                throw new IllegalArgumentException("Unknown delivery method: " + name);
            }
            methods.add(method.getName());
        }
        return methods;
    }
}
