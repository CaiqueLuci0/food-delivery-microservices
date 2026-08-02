package food.delivery.restaurant_ms.core.application.ports.out;

import java.util.UUID;

public interface RestaurantDeletedEventOutputPort {

    void publish(UUID restaurantId);
}
