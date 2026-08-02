package food.delivery.catalog_ms.infra.config;

import food.delivery.catalog_ms.core.application.ports.in.RestaurantReferenceUseCaseInputPort;
import food.delivery.catalog_ms.core.application.ports.out.RestaurantReferenceRepositoryOutputPort;
import food.delivery.catalog_ms.core.application.usecases.RestaurantReferenceUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RestaurantReferenceUseCaseConfig {

    @Bean
    RestaurantReferenceUseCaseInputPort restaurantReferenceUseCase(
            RestaurantReferenceRepositoryOutputPort restaurantReferenceRepositoryOutputPort
    ) {
        return new RestaurantReferenceUseCase(restaurantReferenceRepositoryOutputPort);
    }
}
