package food.delivery.restaurant_ms.infra.adapters.outbound.messaging.producers;

import food.delivery.restaurant_ms.core.application.ports.out.RestaurantCreatedEventOutputPort;
import food.delivery.restaurant_ms.infra.adapters.outbound.messaging.event.RestaurantCreatedEvent;
import food.delivery.restaurant_ms.infra.config.MessagingProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RestaurantCreatedEventProducer implements RestaurantCreatedEventOutputPort {

    private final RabbitTemplate rabbitTemplate;
    private final MessagingProperties messagingProperties;

    public RestaurantCreatedEventProducer(RabbitTemplate rabbitTemplate, MessagingProperties messagingProperties) {
        this.rabbitTemplate = rabbitTemplate;
        this.messagingProperties = messagingProperties;
    }

    @Override
    public void publish(UUID restaurantId, UUID ownerId) {
        rabbitTemplate.convertAndSend(
                messagingProperties.getRestaurantCreatedQueue(),
                new RestaurantCreatedEvent(restaurantId, ownerId)
        );
    }
}
