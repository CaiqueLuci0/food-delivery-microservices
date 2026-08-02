package food.delivery.order_ms.infra.adapters.inbound.messaging.consumers;

import food.delivery.order_ms.core.application.ports.in.UserReferenceUseCaseInputPort;
import food.delivery.order_ms.infra.adapters.inbound.messaging.event.UserCreatedEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class UserCreatedEventConsumer {

    private final UserReferenceUseCaseInputPort userReferenceUseCase;

    public UserCreatedEventConsumer(UserReferenceUseCaseInputPort userReferenceUseCase) {
        this.userReferenceUseCase = userReferenceUseCase;
    }

    @Transactional
    @RabbitListener(queues = "${app.messaging.user-created-queue}")
    public void onUserCreated(UserCreatedEvent event) {
        if (event == null || event.userId() == null) {
            return;
        }
        userReferenceUseCase.handleUserCreated(event.userId());
    }
}
