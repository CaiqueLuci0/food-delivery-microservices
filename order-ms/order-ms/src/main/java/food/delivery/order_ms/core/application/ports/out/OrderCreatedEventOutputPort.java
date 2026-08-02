package food.delivery.order_ms.core.application.ports.out;

import java.util.UUID;

public interface OrderCreatedEventOutputPort {

    void publish(UUID eventId, UUID orderId, UUID userId, String status, Double price);
}
