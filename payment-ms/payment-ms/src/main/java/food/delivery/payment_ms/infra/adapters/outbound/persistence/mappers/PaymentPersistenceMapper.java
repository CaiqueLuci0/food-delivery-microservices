package food.delivery.payment_ms.infra.adapters.outbound.persistence.mappers;

import food.delivery.payment_ms.core.domain.entities.Payment;
import food.delivery.payment_ms.infra.adapters.outbound.persistence.documents.PaymentDocument;

public final class PaymentPersistenceMapper {

    private PaymentPersistenceMapper() {
    }

    public static PaymentDocument toDocument(Payment payment) {
        PaymentDocument document = new PaymentDocument();
        document.setId(payment.getId());
        document.setOrderId(payment.getOrderId());
        document.setUserId(payment.getUserId());
        document.setStatus(payment.getStatus());
        document.setPrice(payment.getPrice());
        document.setStripeId(payment.getStripeId());
        return document;
    }

    public static Payment toDomain(PaymentDocument document) {
        Payment payment = new Payment();
        payment.setId(document.getId());
        payment.setOrderId(document.getOrderId());
        payment.setUserId(document.getUserId());
        payment.setStatus(document.getStatus());
        payment.setPrice(document.getPrice());
        payment.setStripeId(document.getStripeId());
        return payment;
    }
}
