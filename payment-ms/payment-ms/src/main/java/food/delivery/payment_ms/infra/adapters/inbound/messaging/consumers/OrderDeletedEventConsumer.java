package food.delivery.payment_ms.infra.adapters.inbound.messaging.consumers;

import food.delivery.payment_ms.core.application.ports.in.PaymentUseCaseInputPort;
import food.delivery.payment_ms.infra.adapters.inbound.messaging.event.OrderDeletedEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class OrderDeletedEventConsumer {

    private final PaymentUseCaseInputPort paymentUseCase;

    public OrderDeletedEventConsumer(PaymentUseCaseInputPort paymentUseCase) {
        this.paymentUseCase = paymentUseCase;
    }

    @RabbitListener(queues = "${app.messaging.order-deleted-queue}")
    public void onOrderDeleted(OrderDeletedEvent event) {
        if (event == null || event.orderId() == null) {
            return;
        }
        paymentUseCase.onOrderDeleted(event.orderId());
    }
}
