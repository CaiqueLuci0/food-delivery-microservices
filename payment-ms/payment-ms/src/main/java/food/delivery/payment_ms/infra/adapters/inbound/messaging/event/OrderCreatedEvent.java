package food.delivery.payment_ms.infra.adapters.inbound.messaging.event;

import java.util.UUID;

public record OrderCreatedEvent(UUID id, UUID orderId, UUID userId, String status, Double price) {
}
