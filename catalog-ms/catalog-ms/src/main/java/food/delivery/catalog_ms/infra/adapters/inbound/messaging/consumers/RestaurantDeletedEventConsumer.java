package food.delivery.catalog_ms.infra.adapters.inbound.messaging.consumers;

import food.delivery.catalog_ms.core.application.ports.in.RestaurantReferenceUseCaseInputPort;
import food.delivery.catalog_ms.infra.adapters.inbound.messaging.event.RestaurantDeletedEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class RestaurantDeletedEventConsumer {

    private final RestaurantReferenceUseCaseInputPort restaurantReferenceUseCase;

    public RestaurantDeletedEventConsumer(RestaurantReferenceUseCaseInputPort restaurantReferenceUseCase) {
        this.restaurantReferenceUseCase = restaurantReferenceUseCase;
    }

    @Transactional
    @RabbitListener(queues = "${app.messaging.restaurant-deleted-queue}")
    public void onRestaurantDeleted(RestaurantDeletedEvent event) {
        if (event == null || event.restaurantId() == null) {
            return;
        }
        restaurantReferenceUseCase.handleRestaurantDeleted(event.restaurantId());
    }
}
