package food.delivery.user_ms.infra.adapters.outbound.messaging.event;

import java.util.UUID;

public record UserDeletedEvent(UUID userId) {
}
