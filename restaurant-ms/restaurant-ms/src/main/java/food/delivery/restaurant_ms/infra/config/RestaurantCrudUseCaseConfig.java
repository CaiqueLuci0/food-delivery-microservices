package food.delivery.restaurant_ms.infra.config;

import food.delivery.restaurant_ms.core.application.ports.in.RestaurantCrudUseCaseInputPort;
import food.delivery.restaurant_ms.core.application.ports.out.CepLookupOutputPort;
import food.delivery.restaurant_ms.core.application.ports.out.RestaurantCreatedEventOutputPort;
import food.delivery.restaurant_ms.core.application.ports.out.RestaurantDeletedEventOutputPort;
import food.delivery.restaurant_ms.core.application.ports.out.RestaurantRepositoryOutputPort;
import food.delivery.restaurant_ms.core.application.ports.out.UserReferenceRepositoryOutputPort;
import food.delivery.restaurant_ms.core.application.usecases.RestaurantCrudUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RestaurantCrudUseCaseConfig {

    @Bean
    RestaurantCrudUseCaseInputPort restaurantCrudUseCase(
            RestaurantRepositoryOutputPort restaurantRepositoryOutputPort,
            UserReferenceRepositoryOutputPort userReferenceRepositoryOutputPort,
            CepLookupOutputPort cepLookupOutputPort,
            RestaurantCreatedEventOutputPort restaurantCreatedEventOutputPort,
            RestaurantDeletedEventOutputPort restaurantDeletedEventOutputPort
    ) {
        return new RestaurantCrudUseCase(
                restaurantRepositoryOutputPort,
                userReferenceRepositoryOutputPort,
                cepLookupOutputPort,
                restaurantCreatedEventOutputPort,
                restaurantDeletedEventOutputPort
        );
    }
}
