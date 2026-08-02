package food.delivery.payment_ms.core.domain.entities;

import food.delivery.payment_ms.core.domain.enums.PaymentStatus;

import java.util.Objects;
import java.util.UUID;

public class Payment {

    private UUID id;
    private UUID orderId;
    private UUID userId;
    private PaymentStatus status;
    private Double price;
    private String stripeId;

    public static Payment createFromOrderCreated(UUID orderId, UUID userId, Double price) {
        Payment payment = new Payment();
        payment.setId(UUID.randomUUID());
        payment.setOrderId(orderId);
        payment.setUserId(userId);
        payment.setStatus(PaymentStatus.AGUARDANDO);
        payment.setPrice(price);
        return payment;
    }

    public void markAsPaid() {
        this.status = PaymentStatus.PAGO;
    }

    public void cancel() {
        this.status = PaymentStatus.CANCELADO;
    }

    public boolean belongsTo(UUID userId) {
        return Objects.equals(this.userId, userId);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getStripeId() {
        return stripeId;
    }

    public void setStripeId(String stripeId) {
        this.stripeId = stripeId;
    }
}
