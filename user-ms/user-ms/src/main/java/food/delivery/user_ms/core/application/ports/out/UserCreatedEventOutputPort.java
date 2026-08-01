package food.delivery.user_ms.core.application.ports.out;

import java.util.UUID;

public interface UserCreatedEventOutputPort {
    void publish(UUID userId);
}
