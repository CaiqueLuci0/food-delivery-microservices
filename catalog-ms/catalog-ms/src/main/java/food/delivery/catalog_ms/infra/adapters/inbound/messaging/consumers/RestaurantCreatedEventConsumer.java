package food.delivery.catalog_ms.infra.adapters.inbound.messaging.consumers;

import food.delivery.catalog_ms.core.application.ports.in.RestaurantReferenceUseCaseInputPort;
import food.delivery.catalog_ms.infra.adapters.inbound.messaging.event.RestaurantCreatedEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class RestaurantCreatedEventConsumer {

    private final RestaurantReferenceUseCaseInputPort restaurantReferenceUseCase;

    public RestaurantCreatedEventConsumer(RestaurantReferenceUseCaseInputPort restaurantReferenceUseCase) {
        this.restaurantReferenceUseCase = restaurantReferenceUseCase;
    }

    @Transactional
    @RabbitListener(queues = "${app.messaging.restaurant-created-queue}")
    public void onRestaurantCreated(RestaurantCreatedEvent event) {
        if (event == null || event.restaurantId() == null || event.ownerId() == null) {
            return;
        }
        restaurantReferenceUseCase.handleRestaurantCreated(event.restaurantId(), event.ownerId());
    }
}
