package food.delivery.payment_ms.infra.adapters.outbound.messaging.producers;

import food.delivery.payment_ms.core.application.ports.out.PaymentFailedEventOutputPort;
import food.delivery.payment_ms.infra.adapters.outbound.messaging.event.PaymentFailedEvent;
import food.delivery.payment_ms.infra.config.MessagingProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class PaymentFailedEventProducer implements PaymentFailedEventOutputPort {

    private final RabbitTemplate rabbitTemplate;
    private final MessagingProperties messagingProperties;

    public PaymentFailedEventProducer(RabbitTemplate rabbitTemplate, MessagingProperties messagingProperties) {
        this.rabbitTemplate = rabbitTemplate;
        this.messagingProperties = messagingProperties;
    }

    @Override
    public void publish(UUID orderId) {
        rabbitTemplate.convertAndSend(
                messagingProperties.getPaymentFailedQueue(),
                new PaymentFailedEvent(orderId)
        );
    }
}
