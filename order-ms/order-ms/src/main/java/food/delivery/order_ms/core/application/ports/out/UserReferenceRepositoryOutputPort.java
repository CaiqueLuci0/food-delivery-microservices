package food.delivery.order_ms.core.application.ports.out;

import food.delivery.order_ms.core.domain.entities.UserReference;

import java.util.Optional;
import java.util.UUID;

public interface UserReferenceRepositoryOutputPort {

    UserReference save(UserReference userReference);

    Optional<UserReference> findById(UUID id);

    boolean existsById(UUID id);

    void delete(UserReference userReference);
}
