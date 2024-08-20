package ua.everybuy.buisnesslogic.strategy.delivery;

import org.springframework.stereotype.Component;

@Component
public interface DeliveryMethod {
    String getName();
}
