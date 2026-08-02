package food.delivery.order_ms.infra.adapters.inbound.messaging.event;

import java.util.UUID;

public record UserDeletedEvent(UUID userId) {
}
