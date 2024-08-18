package ua.everybuy.buisnesslogic.delivery;

import ua.everybuy.database.entity.DeliveryMethod;

public class MeestExpress implements DeliveryMethodHandler {
    @Override
    public DeliveryMethod getName() {
        return DeliveryMethod.MEEST_EXPRESS;
    }
}
