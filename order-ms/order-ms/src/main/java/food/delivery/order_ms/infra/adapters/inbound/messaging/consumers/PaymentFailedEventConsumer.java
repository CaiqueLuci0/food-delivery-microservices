package food.delivery.order_ms.infra.adapters.inbound.messaging.consumers;

import food.delivery.order_ms.core.application.ports.in.PaymentStatusUseCaseInputPort;
import food.delivery.order_ms.infra.adapters.inbound.messaging.event.PaymentEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class PaymentFailedEventConsumer {

    private final PaymentStatusUseCaseInputPort paymentStatusUseCase;

    public PaymentFailedEventConsumer(PaymentStatusUseCaseInputPort paymentStatusUseCase) {
        this.paymentStatusUseCase = paymentStatusUseCase;
    }

    @Transactional
    @RabbitListener(queues = "${app.messaging.payment-failed-queue}")
    public void onPaymentFailed(PaymentEvent event) {
        if (event == null || event.orderId() == null) {
            return;
        }
        paymentStatusUseCase.onFailed(event.orderId());
    }
}
