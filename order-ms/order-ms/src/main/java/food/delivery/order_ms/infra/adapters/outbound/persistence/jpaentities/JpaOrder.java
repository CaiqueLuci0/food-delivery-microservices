package food.delivery.order_ms.infra.adapters.outbound.persistence.jpaentities;

import food.delivery.order_ms.core.domain.enums.OrderStatus;
import food.delivery.order_ms.core.domain.enums.PaymentStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "order")
public class JpaOrder {

    @Id
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status")
    private PaymentStatus paymentStatus;

    private Double score;

    @Column(name = "restaurant_id", nullable = false)
    private UUID restaurantId;

    @Column(name = "restaurant_owner_id", nullable = false)
    private UUID restaurantOwnerId;

    @Column(name = "client_id")
    private UUID clientId;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Fetch(FetchMode.SUBSELECT)
    private List<JpaProductSnapshot> productSnapshots = new ArrayList<>();

    @PrePersist
    void prePersist() {
        if (id == null) {
            id = UUID.randomUUID();
        }
    }

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

    public List<JpaProductSnapshot> getProductSnapshots() {
        return productSnapshots;
    }

    public void setProductSnapshots(List<JpaProductSnapshot> productSnapshots) {
        this.productSnapshots = productSnapshots != null ? productSnapshots : new ArrayList<>();
    }
}
