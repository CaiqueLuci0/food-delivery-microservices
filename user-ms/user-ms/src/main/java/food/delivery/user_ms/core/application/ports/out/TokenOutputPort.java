package food.delivery.user_ms.core.application.ports.out;

import food.delivery.user_ms.core.domain.entities.User;

import java.util.Optional;
import java.util.UUID;

public interface TokenOutputPort {
    String generate(User user);
    Optional<UUID> extractUserId(String token);
}
