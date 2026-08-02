package food.delivery.payment_ms.infra.adapters.inbound.web.presenter.dto.paymentcontroller;

import food.delivery.payment_ms.core.domain.entities.Payment;

public final class PaymentResponseMapper {

    private PaymentResponseMapper() {
    }

    public static PaymentResponseDto toResponse(Payment payment) {
        PaymentResponseDto dto = new PaymentResponseDto();
        dto.setId(payment.getId());
        dto.setOrderId(payment.getOrderId());
        dto.setUserId(payment.getUserId());
        dto.setStatus(payment.getStatus());
        dto.setPrice(payment.getPrice());
        dto.setStripeId(payment.getStripeId());
        return dto;
    }
}
