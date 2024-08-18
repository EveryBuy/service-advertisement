package ua.everybuy.buisnesslogic.delivery;

import org.springframework.stereotype.Component;
import ua.everybuy.database.entity.DeliveryMethod;

@Component
public interface DeliveryMethodHandler {
    DeliveryMethod getName();
}
