package food.delivery.payment_ms.infra.adapters.inbound.messaging.consumers;

import food.delivery.payment_ms.core.application.ports.in.PaymentUseCaseInputPort;
import food.delivery.payment_ms.infra.adapters.inbound.messaging.event.OrderCreatedEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class OrderCreatedEventConsumer {

    private final PaymentUseCaseInputPort paymentUseCase;

    public OrderCreatedEventConsumer(PaymentUseCaseInputPort paymentUseCase) {
        this.paymentUseCase = paymentUseCase;
    }

    @RabbitListener(queues = "${app.messaging.order-created-queue}")
    public void onOrderCreated(OrderCreatedEvent event) {
        if (event == null) {
            return;
        }
        paymentUseCase.onOrderCreated(event.orderId(), event.userId(), event.price());
    }
}
