package food.delivery.restaurant_ms.core.application.ports.in;

import food.delivery.restaurant_ms.core.domain.entities.Address;
import food.delivery.restaurant_ms.core.domain.entities.Restaurant;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface RestaurantCrudUseCaseInputPort {

    Restaurant findById(UUID id);

    List<Restaurant> findAll(String search, BigDecimal latitude, BigDecimal longitude);

    Restaurant create(UUID authenticatedUserId, Restaurant restaurant, Address address);

    Restaurant update(UUID authenticatedUserId, UUID restaurantId, Restaurant restaurant, Address address);

    void delete(UUID authenticatedUserId, UUID restaurantId);
}
