package food.delivery.order_ms.core.application.ports.in;

import food.delivery.order_ms.core.domain.entities.Order;
import food.delivery.order_ms.core.domain.enums.OrderStatus;

import java.util.List;
import java.util.UUID;

public interface OrderUseCaseInputPort {

    Order create(UUID clientId, List<OrderItem> items, String bearerToken);

    Order update(UUID clientId, UUID orderId, List<OrderItem> items, String bearerToken);

    Order finalize(UUID clientId, UUID orderId);

    Order updateStatus(UUID ownerId, UUID orderId, OrderStatus status);

    Order cancel(UUID userId, UUID orderId);

    Order findById(UUID userId, UUID id);

    List<Order> findAll(UUID userId, UUID restaurantId);

    record OrderItem(UUID productId, List<UUID> specOptionIds) {
    }
}
