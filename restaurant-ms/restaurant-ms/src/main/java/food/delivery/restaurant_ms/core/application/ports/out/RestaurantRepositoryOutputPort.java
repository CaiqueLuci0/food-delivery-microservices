package food.delivery.restaurant_ms.core.application.ports.out;

import food.delivery.restaurant_ms.core.domain.entities.Restaurant;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RestaurantRepositoryOutputPort {

    Restaurant save(Restaurant restaurant);

    Optional<Restaurant> findById(UUID id);

    Optional<Restaurant> findByOwnerId(UUID ownerId);

    List<Restaurant> findAll();

    List<Restaurant> searchByNameOrDescription(String search);

    void delete(Restaurant restaurant);

    boolean existsByOwnerId(UUID ownerId);
}
