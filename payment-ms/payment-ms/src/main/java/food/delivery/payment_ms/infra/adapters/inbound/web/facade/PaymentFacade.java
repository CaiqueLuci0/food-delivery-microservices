package food.delivery.payment_ms.infra.adapters.inbound.web.facade;

import food.delivery.payment_ms.core.application.ports.in.PaymentUseCaseInputPort;
import food.delivery.payment_ms.core.domain.entities.Payment;
import food.delivery.payment_ms.infra.adapters.inbound.web.presenter.dto.paymentcontroller.PaymentResponseDto;
import food.delivery.payment_ms.infra.adapters.inbound.web.presenter.dto.paymentcontroller.PaymentResponseMapper;
import food.delivery.payment_ms.infra.adapters.inbound.web.security.AuthenticatedUser;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class PaymentFacade {

    private final PaymentUseCaseInputPort paymentUseCase;

    public PaymentFacade(PaymentUseCaseInputPort paymentUseCase) {
        this.paymentUseCase = paymentUseCase;
    }

    public PaymentResponseDto markAsPaid(UUID orderId) {
        Payment payment = paymentUseCase.markAsPaid(AuthenticatedUser.requireId(), orderId);
        return PaymentResponseMapper.toResponse(payment);
    }
}
