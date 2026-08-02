package food.delivery.restaurant_ms.core.application.ports.in;

import java.util.UUID;

public interface UserReferenceUseCaseInputPort {

    void handleUserCreated(UUID userId);

    void handleUserDeleted(UUID userId);
}
