package food.delivery.order_ms.infra.config;

import food.delivery.order_ms.core.application.ports.in.PaymentStatusUseCaseInputPort;
import food.delivery.order_ms.core.application.ports.out.OrderRepositoryOutputPort;
import food.delivery.order_ms.core.application.usecases.PaymentStatusUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PaymentStatusUseCaseConfig {

    @Bean
    PaymentStatusUseCaseInputPort paymentStatusUseCase(OrderRepositoryOutputPort orderRepositoryOutputPort) {
        return new PaymentStatusUseCase(orderRepositoryOutputPort);
    }
}
