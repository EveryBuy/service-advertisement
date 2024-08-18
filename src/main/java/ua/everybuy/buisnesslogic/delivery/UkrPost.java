package ua.everybuy.buisnesslogic.delivery;

import ua.everybuy.database.entity.DeliveryMethod;

public class UkrPost implements DeliveryMethodHandler {
    @Override
    public DeliveryMethod getName() {
        return DeliveryMethod.UKR_POST;
    }
}
