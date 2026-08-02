package food.delivery.payment_ms.core.application.ports.in;

import food.delivery.payment_ms.core.domain.entities.Payment;

import java.util.UUID;

public interface PaymentUseCaseInputPort {

    void onOrderCreated(UUID orderId, UUID userId, Double price);

    void onOrderDeleted(UUID orderId);

    Payment markAsPaid(UUID userId, UUID orderId);
}
