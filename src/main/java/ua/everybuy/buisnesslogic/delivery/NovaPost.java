package ua.everybuy.buisnesslogic.delivery;

import ua.everybuy.database.entity.DeliveryMethod;

public class NovaPost implements DeliveryMethodHandler {
    @Override
    public DeliveryMethod getName() {
        return DeliveryMethod.NOVA_POST;
    }
}
