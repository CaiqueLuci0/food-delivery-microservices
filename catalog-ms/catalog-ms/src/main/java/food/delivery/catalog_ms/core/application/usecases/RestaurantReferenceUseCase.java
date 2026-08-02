package food.delivery.catalog_ms.core.application.usecases;

import food.delivery.catalog_ms.core.application.ports.in.RestaurantReferenceUseCaseInputPort;
import food.delivery.catalog_ms.core.application.ports.out.RestaurantReferenceRepositoryOutputPort;
import food.delivery.catalog_ms.core.domain.entities.RestaurantReference;

import java.util.UUID;

public class RestaurantReferenceUseCase implements RestaurantReferenceUseCaseInputPort {

    private final RestaurantReferenceRepositoryOutputPort restaurantReferenceRepositoryOutputPort;

    public RestaurantReferenceUseCase(
            RestaurantReferenceRepositoryOutputPort restaurantReferenceRepositoryOutputPort
    ) {
        this.restaurantReferenceRepositoryOutputPort = restaurantReferenceRepositoryOutputPort;
    }

    @Override
    public void handleRestaurantCreated(UUID restaurantId, UUID ownerId) {
        if (restaurantId == null || ownerId == null) {
            return;
        }
        if (restaurantReferenceRepositoryOutputPort.existsByOwnerIdAndRestaurantId(ownerId, restaurantId)) {
            return;
        }
        restaurantReferenceRepositoryOutputPort.save(new RestaurantReference(ownerId, restaurantId));
    }

    @Override
    public void handleRestaurantDeleted(UUID restaurantId) {
        if (restaurantId == null) {
            return;
        }
        restaurantReferenceRepositoryOutputPort.deleteByRestaurantId(restaurantId);
    }
}
