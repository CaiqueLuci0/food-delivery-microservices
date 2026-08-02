package food.delivery.order_ms.infra.config;

import food.delivery.order_ms.core.application.ports.in.UserReferenceUseCaseInputPort;
import food.delivery.order_ms.core.application.ports.out.UserReferenceRepositoryOutputPort;
import food.delivery.order_ms.core.application.usecases.UserReferenceUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserReferenceUseCaseConfig {

    @Bean
    UserReferenceUseCaseInputPort userReferenceUseCase(
            UserReferenceRepositoryOutputPort userReferenceRepositoryOutputPort
    ) {
        return new UserReferenceUseCase(userReferenceRepositoryOutputPort);
    }
}
