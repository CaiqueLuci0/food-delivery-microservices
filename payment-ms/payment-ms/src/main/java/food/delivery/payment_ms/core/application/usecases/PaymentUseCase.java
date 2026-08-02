package food.delivery.payment_ms.core.application.usecases;

import food.delivery.payment_ms.core.application.ports.in.PaymentUseCaseInputPort;
import food.delivery.payment_ms.core.application.ports.out.PaymentApprovedEventOutputPort;
import food.delivery.payment_ms.core.application.ports.out.PaymentRepositoryOutputPort;
import food.delivery.payment_ms.core.domain.entities.Payment;
import food.delivery.payment_ms.core.domain.enums.ConstMessagesEnum;
import food.delivery.payment_ms.core.domain.enums.PaymentStatus;
import food.delivery.payment_ms.core.domain.exceptions.ConflictException;
import food.delivery.payment_ms.core.domain.exceptions.ForbiddenException;
import food.delivery.payment_ms.core.domain.exceptions.NotFoundException;

import java.util.UUID;

public class PaymentUseCase implements PaymentUseCaseInputPort {

    private final PaymentRepositoryOutputPort paymentRepositoryOutputPort;
    private final PaymentApprovedEventOutputPort paymentApprovedEventOutputPort;

    public PaymentUseCase(
            PaymentRepositoryOutputPort paymentRepositoryOutputPort,
            PaymentApprovedEventOutputPort paymentApprovedEventOutputPort
    ) {
        this.paymentRepositoryOutputPort = paymentRepositoryOutputPort;
        this.paymentApprovedEventOutputPort = paymentApprovedEventOutputPort;
    }

    @Override
    public void onOrderCreated(UUID orderId, UUID userId, Double price) {
        if (orderId == null || userId == null) {
            return;
        }
        if (paymentRepositoryOutputPort.existsByOrderId(orderId)) {
            return;
        }
        paymentRepositoryOutputPort.save(Payment.createFromOrderCreated(orderId, userId, price));
    }

    @Override
    public void onOrderDeleted(UUID orderId) {
        if (orderId == null) {
            return;
        }
        paymentRepositoryOutputPort.findByOrderId(orderId).ifPresent(payment -> {
            if (payment.getStatus() == PaymentStatus.PAGO) {
                return;
            }
            payment.cancel();
            paymentRepositoryOutputPort.save(payment);
        });
    }

    @Override
    public Payment markAsPaid(UUID userId, UUID orderId) {
        Payment payment = paymentRepositoryOutputPort.findByOrderId(orderId)
                .orElseThrow(() -> new NotFoundException(ConstMessagesEnum.NOT_FOUND.getMessage()));

        if (!payment.belongsTo(userId)) {
            throw new ForbiddenException(ConstMessagesEnum.ACCESS_DENIED.getMessage());
        }
        if (payment.getStatus() != PaymentStatus.AGUARDANDO) {
            throw new ConflictException(ConstMessagesEnum.INVALID_PAYMENT_STATUS.getMessage());
        }

        payment.markAsPaid();
        Payment saved = paymentRepositoryOutputPort.save(payment);
        paymentApprovedEventOutputPort.publish(saved.getOrderId());
        return saved;
    }
}
