package food.delivery.user_ms.core.application.ports.in;

import food.delivery.user_ms.core.domain.entities.UserLoginDetails;

public interface AuthenticateUseCaseInputPort {
    UserLoginDetails login(String email, String password);
}
