package food.delivery.order_ms.infra.config;

import food.delivery.order_ms.core.application.ports.in.OrderUseCaseInputPort;
import food.delivery.order_ms.core.application.ports.out.CatalogResolveOutputPort;
import food.delivery.order_ms.core.application.ports.out.OrderCreatedEventOutputPort;
import food.delivery.order_ms.core.application.ports.out.OrderDeletedEventOutputPort;
import food.delivery.order_ms.core.application.ports.out.OrderRepositoryOutputPort;
import food.delivery.order_ms.core.application.ports.out.UserReferenceRepositoryOutputPort;
import food.delivery.order_ms.core.application.usecases.OrderUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderUseCaseConfig {

    @Bean
    OrderUseCaseInputPort orderUseCase(
            OrderRepositoryOutputPort orderRepositoryOutputPort,
            UserReferenceRepositoryOutputPort userReferenceRepositoryOutputPort,
            CatalogResolveOutputPort catalogResolveOutputPort,
            OrderCreatedEventOutputPort orderCreatedEventOutputPort,
            OrderDeletedEventOutputPort orderDeletedEventOutputPort
    ) {
        return new OrderUseCase(
                orderRepositoryOutputPort,
                userReferenceRepositoryOutputPort,
                catalogResolveOutputPort,
                orderCreatedEventOutputPort,
                orderDeletedEventOutputPort
        );
    }
}
