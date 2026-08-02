package food.delivery.payment_ms.infra.config;

import food.delivery.payment_ms.core.application.ports.in.PaymentUseCaseInputPort;
import food.delivery.payment_ms.core.application.ports.out.PaymentApprovedEventOutputPort;
import food.delivery.payment_ms.core.application.ports.out.PaymentRepositoryOutputPort;
import food.delivery.payment_ms.core.application.usecases.PaymentUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PaymentUseCaseConfig {

    @Bean
    PaymentUseCaseInputPort paymentUseCase(
            PaymentRepositoryOutputPort paymentRepositoryOutputPort,
            PaymentApprovedEventOutputPort paymentApprovedEventOutputPort
    ) {
        return new PaymentUseCase(paymentRepositoryOutputPort, paymentApprovedEventOutputPort);
    }
}
