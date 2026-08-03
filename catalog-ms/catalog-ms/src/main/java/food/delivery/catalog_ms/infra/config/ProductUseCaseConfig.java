package food.delivery.catalog_ms.infra.config;

import food.delivery.catalog_ms.core.application.ports.in.ProductCrudUseCaseInputPort;
import food.delivery.catalog_ms.core.application.ports.in.ProductResolveUseCaseInputPort;
import food.delivery.catalog_ms.core.application.ports.out.ObjectStorageOutputPort;
import food.delivery.catalog_ms.core.application.ports.out.ProductRepositoryOutputPort;
import food.delivery.catalog_ms.core.application.ports.out.RestaurantReferenceRepositoryOutputPort;
import food.delivery.catalog_ms.core.application.usecases.ProductCrudUseCase;
import food.delivery.catalog_ms.core.application.usecases.ProductResolveUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProductUseCaseConfig {

    @Bean
    ProductCrudUseCaseInputPort productCrudUseCase(
            ProductRepositoryOutputPort productRepositoryOutputPort,
            RestaurantReferenceRepositoryOutputPort restaurantReferenceRepositoryOutputPort,
            ObjectStorageOutputPort objectStorageOutputPort
    ) {
        return new ProductCrudUseCase(
                productRepositoryOutputPort,
                restaurantReferenceRepositoryOutputPort,
                objectStorageOutputPort
        );
    }

    @Bean
    ProductResolveUseCaseInputPort productResolveUseCase(ProductRepositoryOutputPort productRepositoryOutputPort) {
        return new ProductResolveUseCase(productRepositoryOutputPort);
    }
}
