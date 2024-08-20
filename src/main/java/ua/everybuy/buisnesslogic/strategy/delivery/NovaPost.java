package ua.everybuy.buisnesslogic.strategy.delivery;

public class NovaPost implements DeliveryMethod {
    @Override
    public String getName() {
        return "NOVA_POST";
    }
}
