package food.delivery.order_ms.core.application.ports.out;

import food.delivery.order_ms.core.domain.entities.Order;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepositoryOutputPort {

    Order save(Order order);

    Order saveReplacingSnapshots(Order order);

    Optional<Order> findById(UUID id);

    List<Order> findByClientId(UUID clientId);

    List<Order> findByRestaurantIdAndOwnerId(UUID restaurantId, UUID ownerId);

    void delete(Order order);
}
