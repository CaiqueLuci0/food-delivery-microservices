package food.delivery.order_ms.infra.adapters.outbound.messaging.event;

import java.util.UUID;

public record OrderCreatedEvent(UUID id, UUID orderId, UUID userId, String status, Double price) {
}
