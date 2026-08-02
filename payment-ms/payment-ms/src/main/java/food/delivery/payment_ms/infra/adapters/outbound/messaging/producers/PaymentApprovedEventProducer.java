package food.delivery.payment_ms.infra.adapters.outbound.messaging.producers;

import food.delivery.payment_ms.core.application.ports.out.PaymentApprovedEventOutputPort;
import food.delivery.payment_ms.infra.adapters.outbound.messaging.event.PaymentApprovedEvent;
import food.delivery.payment_ms.infra.config.MessagingProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class PaymentApprovedEventProducer implements PaymentApprovedEventOutputPort {

    private final RabbitTemplate rabbitTemplate;
    private final MessagingProperties messagingProperties;

    public PaymentApprovedEventProducer(RabbitTemplate rabbitTemplate, MessagingProperties messagingProperties) {
        this.rabbitTemplate = rabbitTemplate;
        this.messagingProperties = messagingProperties;
    }

    @Override
    public void publish(UUID orderId) {
        rabbitTemplate.convertAndSend(
                messagingProperties.getPaymentApprovedQueue(),
                new PaymentApprovedEvent(orderId)
        );
    }
}
