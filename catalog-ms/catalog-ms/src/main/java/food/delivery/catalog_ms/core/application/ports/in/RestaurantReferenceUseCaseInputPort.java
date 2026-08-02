package food.delivery.catalog_ms.core.application.ports.in;

import java.util.UUID;

public interface RestaurantReferenceUseCaseInputPort {

    void handleRestaurantCreated(UUID restaurantId, UUID ownerId);

    void handleRestaurantDeleted(UUID restaurantId);
}
