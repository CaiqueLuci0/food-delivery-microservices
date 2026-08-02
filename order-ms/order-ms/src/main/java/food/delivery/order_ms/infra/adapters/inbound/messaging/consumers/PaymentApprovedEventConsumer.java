package food.delivery.order_ms.infra.adapters.inbound.messaging.consumers;

import food.delivery.order_ms.core.application.ports.in.PaymentStatusUseCaseInputPort;
import food.delivery.order_ms.infra.adapters.inbound.messaging.event.PaymentEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class PaymentApprovedEventConsumer {

    private final PaymentStatusUseCaseInputPort paymentStatusUseCase;

    public PaymentApprovedEventConsumer(PaymentStatusUseCaseInputPort paymentStatusUseCase) {
        this.paymentStatusUseCase = paymentStatusUseCase;
    }

    @Transactional
    @RabbitListener(queues = "${app.messaging.payment-approved-queue}")
    public void onPaymentApproved(PaymentEvent event) {
        if (event == null || event.orderId() == null) {
            return;
        }
        paymentStatusUseCase.onApproved(event.orderId());
    }
}
