package food.delivery.payment_ms.infra.adapters.outbound.persistence.repositories;

import food.delivery.payment_ms.infra.adapters.outbound.persistence.documents.PaymentDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;
import java.util.UUID;

public interface PaymentMongoRepository extends MongoRepository<PaymentDocument, UUID> {

    Optional<PaymentDocument> findByOrderId(UUID orderId);

    boolean existsByOrderId(UUID orderId);
}
