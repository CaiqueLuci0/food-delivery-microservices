package food.delivery.catalog_ms.core.application.ports.out;

import food.delivery.catalog_ms.core.domain.entities.RestaurantReference;

import java.util.Optional;
import java.util.UUID;

public interface RestaurantReferenceRepositoryOutputPort {

    RestaurantReference save(RestaurantReference restaurantReference);

    Optional<RestaurantReference> findByOwnerId(UUID ownerId);

    Optional<RestaurantReference> findByRestaurantId(UUID restaurantId);

    boolean existsByOwnerIdAndRestaurantId(UUID ownerId, UUID restaurantId);

    void deleteByRestaurantId(UUID restaurantId);
}
