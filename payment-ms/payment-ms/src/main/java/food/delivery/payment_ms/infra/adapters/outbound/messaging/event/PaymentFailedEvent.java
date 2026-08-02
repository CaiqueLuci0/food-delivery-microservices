package food.delivery.payment_ms.infra.adapters.outbound.messaging.event;

import java.util.UUID;

public record PaymentFailedEvent(UUID orderId) {
}
