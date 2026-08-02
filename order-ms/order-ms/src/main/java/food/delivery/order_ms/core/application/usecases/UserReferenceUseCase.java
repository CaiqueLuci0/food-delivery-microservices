package food.delivery.order_ms.core.application.usecases;

import food.delivery.order_ms.core.application.ports.in.UserReferenceUseCaseInputPort;
import food.delivery.order_ms.core.application.ports.out.UserReferenceRepositoryOutputPort;
import food.delivery.order_ms.core.domain.entities.UserReference;

import java.util.UUID;

public class UserReferenceUseCase implements UserReferenceUseCaseInputPort {

    private final UserReferenceRepositoryOutputPort userReferenceRepositoryOutputPort;

    public UserReferenceUseCase(UserReferenceRepositoryOutputPort userReferenceRepositoryOutputPort) {
        this.userReferenceRepositoryOutputPort = userReferenceRepositoryOutputPort;
    }

    @Override
    public void handleUserCreated(UUID userId) {
        if (userId == null || userReferenceRepositoryOutputPort.existsById(userId)) {
            return;
        }
        userReferenceRepositoryOutputPort.save(new UserReference(userId));
    }

    @Override
    public void handleUserDeleted(UUID userId) {
        if (userId == null) {
            return;
        }
        userReferenceRepositoryOutputPort.findById(userId)
                .ifPresent(userReferenceRepositoryOutputPort::delete);
    }
}
