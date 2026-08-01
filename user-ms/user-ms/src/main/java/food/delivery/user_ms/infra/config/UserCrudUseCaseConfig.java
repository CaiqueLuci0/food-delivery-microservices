package food.delivery.user_ms.infra.config;

import food.delivery.user_ms.core.application.ports.in.UserCrudUseCaseInputPort;
import food.delivery.user_ms.core.application.ports.out.CepLookupOutputPort;
import food.delivery.user_ms.core.application.ports.out.PasswordEncoderOutputPort;
import food.delivery.user_ms.core.application.ports.out.UserCreatedEventOutputPort;
import food.delivery.user_ms.core.application.ports.out.UserDeletedEventOutputPort;
import food.delivery.user_ms.core.application.ports.out.UserRepositoryOutputPort;
import food.delivery.user_ms.core.application.usecases.UserCrudUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserCrudUseCaseConfig {

    @Bean
    UserCrudUseCaseInputPort userCrudUseCase(
            UserRepositoryOutputPort repository,
            PasswordEncoderOutputPort passwordEncoderOutputPort,
            UserCreatedEventOutputPort userCreatedEventOutputPort,
            UserDeletedEventOutputPort userDeletedEventOutputPort,
            CepLookupOutputPort cepLookupOutputPort
    ) {
        return new UserCrudUseCase(
                repository,
                passwordEncoderOutputPort,
                userCreatedEventOutputPort,
                userDeletedEventOutputPort,
                cepLookupOutputPort
        );
    }
}
