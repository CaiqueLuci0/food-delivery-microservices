package food.delivery.payment_ms.core.application.ports.out;

import java.util.Optional;
import java.util.UUID;

public interface TokenOutputPort {
    Optional<UUID> extractUserId(String token);
}
