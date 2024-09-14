package ua.everybuy.buisnesslogic.strategy.delivery;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class DeliveryMethodFactory {

    private final Map<String, DeliveryMethod> deliveryMethods = new HashMap<>();

    @PostConstruct
    public void init() {
        registerDeliveryMethod("NOVA_POST", new NovaPost());
        registerDeliveryMethod("UKR_POST", new UkrPost());
        registerDeliveryMethod("MEEST_EXPRESS", new MeestExpress());
    }

    public void registerDeliveryMethod(String name, DeliveryMethod handler) {
        deliveryMethods.put(name.toUpperCase(), handler);
    }

    public Set<DeliveryMethod> getDeliveryMethodsFromNames(Set<String> names) {
        Set<DeliveryMethod> methods = new HashSet<>();
        for (String name : names) {
            String upperCaseName = name.toUpperCase();
            DeliveryMethod methodHandler = deliveryMethods.get(upperCaseName);
            if (methodHandler == null) {
                throw new IllegalArgumentException("Unknown delivery method: " + name);
            }
            methods.add(methodHandler);
        }
        return methods;
    }
}
