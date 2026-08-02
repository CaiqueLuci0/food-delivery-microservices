package food.delivery.restaurant_ms.core.application.usecases;

import food.delivery.restaurant_ms.core.application.ports.in.UserReferenceUseCaseInputPort;
import food.delivery.restaurant_ms.core.application.ports.out.RestaurantDeletedEventOutputPort;
import food.delivery.restaurant_ms.core.application.ports.out.RestaurantRepositoryOutputPort;
import food.delivery.restaurant_ms.core.application.ports.out.UserReferenceRepositoryOutputPort;
import food.delivery.restaurant_ms.core.domain.entities.Restaurant;
import food.delivery.restaurant_ms.core.domain.entities.UserReference;

import java.util.Optional;
import java.util.UUID;

public class UserReferenceUseCase implements UserReferenceUseCaseInputPort {

    private final UserReferenceRepositoryOutputPort userReferenceRepositoryOutputPort;
    private final RestaurantRepositoryOutputPort restaurantRepositoryOutputPort;
    private final RestaurantDeletedEventOutputPort restaurantDeletedEventOutputPort;

    public UserReferenceUseCase(
            UserReferenceRepositoryOutputPort userReferenceRepositoryOutputPort,
            RestaurantRepositoryOutputPort restaurantRepositoryOutputPort,
            RestaurantDeletedEventOutputPort restaurantDeletedEventOutputPort
    ) {
        this.userReferenceRepositoryOutputPort = userReferenceRepositoryOutputPort;
        this.restaurantRepositoryOutputPort = restaurantRepositoryOutputPort;
        this.restaurantDeletedEventOutputPort = restaurantDeletedEventOutputPort;
    }

    @Override
    public void handleUserCreated(UUID userId) {
        if (userId == null || userReferenceRepositoryOutputPort.existsById(userId)) {
            return;
        }
        userReferenceRepositoryOutputPort.save(new UserReference(userId));
    }

    @Override
    public void handleUserDeleted(UUID userId) {
        if (userId == null) {
            return;
        }

        Optional<Restaurant> restaurant = restaurantRepositoryOutputPort.findByOwnerId(userId);
        restaurant.ifPresent(existing -> {
            UUID restaurantId = existing.getId();
            restaurantRepositoryOutputPort.delete(existing);
            restaurantDeletedEventOutputPort.publish(restaurantId);
        });

        userReferenceRepositoryOutputPort.findById(userId)
                .ifPresent(userReferenceRepositoryOutputPort::delete);
    }
}
