package food.delivery.order_ms.core.application.ports.in;

import java.util.UUID;

public interface PaymentStatusUseCaseInputPort {

    void onApproved(UUID orderId);

    void onFailed(UUID orderId);
}
