package food.delivery.payment_ms.infra.adapters.outbound.persistence.adapters;

import food.delivery.payment_ms.core.application.ports.out.PaymentRepositoryOutputPort;
import food.delivery.payment_ms.core.domain.entities.Payment;
import food.delivery.payment_ms.infra.adapters.outbound.persistence.mappers.PaymentPersistenceMapper;
import food.delivery.payment_ms.infra.adapters.outbound.persistence.repositories.PaymentMongoRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class PaymentRepositoryOutputPortAdapter implements PaymentRepositoryOutputPort {

    private final PaymentMongoRepository paymentMongoRepository;

    public PaymentRepositoryOutputPortAdapter(PaymentMongoRepository paymentMongoRepository) {
        this.paymentMongoRepository = paymentMongoRepository;
    }

    @Override
    public Payment save(Payment payment) {
        return PaymentPersistenceMapper.toDomain(
                paymentMongoRepository.save(PaymentPersistenceMapper.toDocument(payment))
        );
    }

    @Override
    public Optional<Payment> findByOrderId(UUID orderId) {
        return paymentMongoRepository.findByOrderId(orderId).map(PaymentPersistenceMapper::toDomain);
    }

    @Override
    public boolean existsByOrderId(UUID orderId) {
        return paymentMongoRepository.existsByOrderId(orderId);
    }
}
