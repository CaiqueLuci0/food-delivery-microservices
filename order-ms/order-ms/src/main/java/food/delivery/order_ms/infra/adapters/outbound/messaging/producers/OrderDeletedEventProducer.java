package food.delivery.order_ms.infra.adapters.outbound.messaging.producers;

import food.delivery.order_ms.core.application.ports.out.OrderDeletedEventOutputPort;
import food.delivery.order_ms.infra.adapters.outbound.messaging.event.OrderDeletedEvent;
import food.delivery.order_ms.infra.config.MessagingProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class OrderDeletedEventProducer implements OrderDeletedEventOutputPort {

    private final RabbitTemplate rabbitTemplate;
    private final MessagingProperties messagingProperties;

    public OrderDeletedEventProducer(RabbitTemplate rabbitTemplate, MessagingProperties messagingProperties) {
        this.rabbitTemplate = rabbitTemplate;
        this.messagingProperties = messagingProperties;
    }

    @Override
    public void publish(UUID orderId) {
        rabbitTemplate.convertAndSend(
                messagingProperties.getOrderDeletedQueue(),
                new OrderDeletedEvent(orderId)
        );
    }
}
