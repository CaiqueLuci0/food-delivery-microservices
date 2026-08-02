package food.delivery.payment_ms.core.application.ports.out;

import java.util.UUID;

public interface PaymentApprovedEventOutputPort {
    void publish(UUID orderId);
}
