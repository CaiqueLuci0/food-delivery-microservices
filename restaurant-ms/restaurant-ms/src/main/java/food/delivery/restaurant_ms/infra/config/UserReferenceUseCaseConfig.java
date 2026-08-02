package food.delivery.restaurant_ms.infra.config;

import food.delivery.restaurant_ms.core.application.ports.in.UserReferenceUseCaseInputPort;
import food.delivery.restaurant_ms.core.application.ports.out.RestaurantDeletedEventOutputPort;
import food.delivery.restaurant_ms.core.application.ports.out.RestaurantRepositoryOutputPort;
import food.delivery.restaurant_ms.core.application.ports.out.UserReferenceRepositoryOutputPort;
import food.delivery.restaurant_ms.core.application.usecases.UserReferenceUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserReferenceUseCaseConfig {

    @Bean
    UserReferenceUseCaseInputPort userReferenceUseCase(
            UserReferenceRepositoryOutputPort userReferenceRepositoryOutputPort,
            RestaurantRepositoryOutputPort restaurantRepositoryOutputPort,
            RestaurantDeletedEventOutputPort restaurantDeletedEventOutputPort
    ) {
        return new UserReferenceUseCase(
                userReferenceRepositoryOutputPort,
                restaurantRepositoryOutputPort,
                restaurantDeletedEventOutputPort
        );
    }
}
