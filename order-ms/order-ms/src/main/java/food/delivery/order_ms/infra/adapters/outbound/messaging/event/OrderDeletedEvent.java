package food.delivery.order_ms.infra.adapters.outbound.messaging.event;

import java.util.UUID;

public record OrderDeletedEvent(UUID orderId) {
}
