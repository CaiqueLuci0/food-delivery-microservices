package food.delivery.order_ms.core.application.ports.out;

import food.delivery.order_ms.core.domain.entities.Order;

public interface OrderStatusPushOutputPort {
    void push(Order order);
}
