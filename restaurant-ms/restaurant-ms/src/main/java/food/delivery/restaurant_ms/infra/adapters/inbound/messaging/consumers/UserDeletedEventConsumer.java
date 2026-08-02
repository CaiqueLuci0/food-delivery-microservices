package food.delivery.restaurant_ms.infra.adapters.inbound.messaging.consumers;

import food.delivery.restaurant_ms.core.application.ports.in.UserReferenceUseCaseInputPort;
import food.delivery.restaurant_ms.infra.adapters.outbound.messaging.event.UserDeletedEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class UserDeletedEventConsumer {

    private final UserReferenceUseCaseInputPort userReferenceUseCase;

    public UserDeletedEventConsumer(UserReferenceUseCaseInputPort userReferenceUseCase) {
        this.userReferenceUseCase = userReferenceUseCase;
    }

    @Transactional
    @RabbitListener(queues = "${app.messaging.user-deleted-queue}")
    public void onUserDeleted(UserDeletedEvent event) {
        if (event == null || event.userId() == null) {
            return;
        }
        userReferenceUseCase.handleUserDeleted(event.userId());
    }
}
