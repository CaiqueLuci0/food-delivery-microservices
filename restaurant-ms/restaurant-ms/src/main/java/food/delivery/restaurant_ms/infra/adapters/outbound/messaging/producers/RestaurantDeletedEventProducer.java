package food.delivery.restaurant_ms.infra.adapters.outbound.messaging.producers;

import food.delivery.restaurant_ms.core.application.ports.out.RestaurantDeletedEventOutputPort;
import food.delivery.restaurant_ms.infra.adapters.outbound.messaging.event.RestaurantDeletedEvent;
import food.delivery.restaurant_ms.infra.config.MessagingProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RestaurantDeletedEventProducer implements RestaurantDeletedEventOutputPort {

    private final RabbitTemplate rabbitTemplate;
    private final MessagingProperties messagingProperties;

    public RestaurantDeletedEventProducer(RabbitTemplate rabbitTemplate, MessagingProperties messagingProperties) {
        this.rabbitTemplate = rabbitTemplate;
        this.messagingProperties = messagingProperties;
    }

    @Override
    public void publish(UUID restaurantId) {
        rabbitTemplate.convertAndSend(
                messagingProperties.getRestaurantDeletedQueue(),
                new RestaurantDeletedEvent(restaurantId)
        );
    }
}
