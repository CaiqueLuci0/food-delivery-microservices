package food.delivery.user_ms.infra.config;

import food.delivery.user_ms.core.application.ports.in.UserCrudUseCaseInputPort;
import food.delivery.user_ms.core.application.ports.out.UserRepositoryOutputPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserCrudUseCaseConfig {

    @Bean
    UserCrudUseCaseInputPort UserCrudUseCase(
            UserRepositoryOutputPort repository
    ) {
        return new food.delivery.user_ms.core.application.usecases.UserCrudUseCase(repository);
    }
}
