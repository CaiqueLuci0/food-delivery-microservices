package food.delivery.user_ms.infra.adapters.outbound.messaging.producers;

import food.delivery.user_ms.core.application.ports.out.UserCreatedEventOutputPort;
import food.delivery.user_ms.infra.adapters.outbound.messaging.event.UserCreatedEvent;
import food.delivery.user_ms.infra.config.MessagingProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UserCreatedEventProducer implements UserCreatedEventOutputPort {

    private final RabbitTemplate rabbitTemplate;
    private final MessagingProperties messagingProperties;

    public UserCreatedEventProducer(RabbitTemplate rabbitTemplate, MessagingProperties messagingProperties) {
        this.rabbitTemplate = rabbitTemplate;
        this.messagingProperties = messagingProperties;
    }

    @Override
    public void publish(UUID userId) {
        rabbitTemplate.convertAndSend(
                messagingProperties.getUserCreatedExchange(),
                "",
                new UserCreatedEvent(userId)
        );
    }
}
