package food.delivery.user_ms.infra.config;

import food.delivery.user_ms.core.application.ports.in.AuthenticateUseCaseInputPort;
import food.delivery.user_ms.core.application.ports.out.PasswordEncoderOutputPort;
import food.delivery.user_ms.core.application.ports.out.TokenOutputPort;
import food.delivery.user_ms.core.application.ports.out.UserRepositoryOutputPort;
import food.delivery.user_ms.core.application.usecases.AuthenticateUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthenticateUseCaseConfig {

    @Bean
    AuthenticateUseCaseInputPort authenticateUseCase(
            UserRepositoryOutputPort userRepositoryOutputPort,
            PasswordEncoderOutputPort passwordEncoderOutputPort,
            TokenOutputPort tokenOutputPort
    ) {
        return new AuthenticateUseCase(userRepositoryOutputPort, passwordEncoderOutputPort, tokenOutputPort);
    }
}
