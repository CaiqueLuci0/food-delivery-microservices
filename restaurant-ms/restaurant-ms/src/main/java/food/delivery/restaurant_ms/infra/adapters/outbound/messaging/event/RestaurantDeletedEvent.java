package food.delivery.restaurant_ms.infra.adapters.outbound.messaging.event;

import java.util.UUID;

public record RestaurantDeletedEvent(UUID restaurantId) {
}
