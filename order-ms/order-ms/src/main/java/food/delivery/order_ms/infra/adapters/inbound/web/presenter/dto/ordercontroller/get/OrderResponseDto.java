package food.delivery.order_ms.infra.adapters.inbound.web.presenter.dto.ordercontroller.get;

import food.delivery.order_ms.core.domain.enums.OrderStatus;
import food.delivery.order_ms.core.domain.enums.PaymentStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OrderResponseDto {

    private UUID id;
    private OrderStatus status;
    private PaymentStatus paymentStatus;
    private Double score;
    private UUID restaurantId;
    private UUID restaurantOwnerId;
    private UUID clientId;
    private List<ProductSnapshotResponseDto> productSnapshots = new ArrayList<>();

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public UUID getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(UUID restaurantId) {
        this.restaurantId = restaurantId;
    }

    public UUID getRestaurantOwnerId() {
        return restaurantOwnerId;
    }

    public void setRestaurantOwnerId(UUID restaurantOwnerId) {
        this.restaurantOwnerId = restaurantOwnerId;
    }

    public UUID getClientId() {
        return clientId;
    }

    public void setClientId(UUID clientId) {
        this.clientId = clientId;
    }

    public List<ProductSnapshotResponseDto> getProductSnapshots() {
        return productSnapshots;
    }

    public void setProductSnapshots(List<ProductSnapshotResponseDto> productSnapshots) {
        this.productSnapshots = productSnapshots != null ? productSnapshots : new ArrayList<>();
    }
}
