package food.delivery.payment_ms.core.application.ports.out;

import food.delivery.payment_ms.core.domain.entities.Payment;

import java.util.Optional;
import java.util.UUID;

public interface PaymentRepositoryOutputPort {
    Payment save(Payment payment);

    Optional<Payment> findByOrderId(UUID orderId);

    boolean existsByOrderId(UUID orderId);
}
