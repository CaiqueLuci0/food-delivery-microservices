package food.delivery.order_ms.core.application.ports.out;

import java.util.UUID;

public interface OrderDeletedEventOutputPort {
    void publish(UUID orderId);
}
