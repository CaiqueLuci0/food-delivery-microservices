package food.delivery.restaurant_ms.core.application.ports.out;

import java.util.Optional;
import java.util.UUID;

public interface TokenOutputPort {

    Optional<UUID> extractUserId(String token);
}
