package food.delivery.catalog_ms.infra.adapters.inbound.messaging.event;

import java.util.UUID;

public record RestaurantCreatedEvent(UUID restaurantId, UUID ownerId) {
}
