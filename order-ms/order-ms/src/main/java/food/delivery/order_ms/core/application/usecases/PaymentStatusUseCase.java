package food.delivery.order_ms.core.application.usecases;

import food.delivery.order_ms.core.application.ports.in.PaymentStatusUseCaseInputPort;
import food.delivery.order_ms.core.application.ports.out.OrderRepositoryOutputPort;
import food.delivery.order_ms.core.domain.enums.OrderStatus;
import food.delivery.order_ms.core.domain.enums.PaymentStatus;

import java.util.UUID;

public class PaymentStatusUseCase implements PaymentStatusUseCaseInputPort {

    private final OrderRepositoryOutputPort orderRepositoryOutputPort;

    public PaymentStatusUseCase(OrderRepositoryOutputPort orderRepositoryOutputPort) {
        this.orderRepositoryOutputPort = orderRepositoryOutputPort;
    }

    @Override
    public void onApproved(UUID orderId) {
        if (orderId == null) {
            return;
        }
        orderRepositoryOutputPort.findById(orderId).ifPresent(order -> {
            order.setPaymentStatus(PaymentStatus.PAGO);
            orderRepositoryOutputPort.save(order);
        });
    }

    @Override
    public void onFailed(UUID orderId) {
        if (orderId == null) {
            return;
        }
        orderRepositoryOutputPort.findById(orderId).ifPresent(order -> {
            order.setPaymentStatus(PaymentStatus.CANCELADO);
            order.setStatus(OrderStatus.CANCELADO);
            orderRepositoryOutputPort.save(order);
        });
    }
}
