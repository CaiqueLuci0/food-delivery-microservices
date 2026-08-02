package food.delivery.order_ms.infra.adapters.outbound.messaging.producers;

import food.delivery.order_ms.core.application.ports.out.OrderCreatedEventOutputPort;
import food.delivery.order_ms.infra.adapters.outbound.messaging.event.OrderCreatedEvent;
import food.delivery.order_ms.infra.config.MessagingProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class OrderCreatedEventProducer implements OrderCreatedEventOutputPort {

    private final RabbitTemplate rabbitTemplate;
    private final MessagingProperties messagingProperties;

    public OrderCreatedEventProducer(RabbitTemplate rabbitTemplate, MessagingProperties messagingProperties) {
        this.rabbitTemplate = rabbitTemplate;
        this.messagingProperties = messagingProperties;
    }

    @Override
    public void publish(UUID eventId, UUID orderId, UUID userId, String status, Double price) {
        rabbitTemplate.convertAndSend(
                messagingProperties.getOrderCreatedQueue(),
                new OrderCreatedEvent(eventId, orderId, userId, status, price)
        );
    }
}
