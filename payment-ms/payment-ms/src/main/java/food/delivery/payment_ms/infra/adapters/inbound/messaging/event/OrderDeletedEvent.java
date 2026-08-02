package food.delivery.payment_ms.infra.adapters.inbound.messaging.event;

import java.util.UUID;

public record OrderDeletedEvent(UUID orderId) {
}
