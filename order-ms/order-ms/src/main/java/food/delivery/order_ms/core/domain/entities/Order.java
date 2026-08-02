package food.delivery.order_ms.core.domain.entities;

import food.delivery.order_ms.core.domain.enums.OrderStatus;
import food.delivery.order_ms.core.domain.enums.PaymentStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Order {

    private UUID id;
    private OrderStatus status;
    private PaymentStatus paymentStatus;
    private Double score;
    private UUID restaurantId;
    private UUID restaurantOwnerId;
    private UUID clientId;
    private List<ProductSnapshot> productSnapshots = new ArrayList<>();

    public static Order create(UUID clientId) {
        Order order = new Order();
        order.setStatus(OrderStatus.EM_CADASTRAMENTO);
        order.setClientId(clientId);
        return order;
    }

    public void applyCatalogResolution(CatalogResolution resolution) {
        this.restaurantId = resolution.getRestaurantId();
        this.restaurantOwnerId = resolution.getRestaurantOwnerId();
        this.productSnapshots = new ArrayList<>(resolution.getProductSnapshots());
        this.productSnapshots.forEach(snapshot -> snapshot.setOrder(this));
    }

    public boolean belongsTo(UUID clientId) {
        return Objects.equals(this.clientId, clientId);
    }

    public boolean isOwnedBy(UUID ownerId) {
        return Objects.equals(this.restaurantOwnerId, ownerId);
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

    public List<ProductSnapshot> getProductSnapshots() {
        return productSnapshots;
    }

    public void setProductSnapshots(List<ProductSnapshot> productSnapshots) {
        this.productSnapshots = productSnapshots != null ? productSnapshots : new ArrayList<>();
    }

    public double computePrice() {
        double total = 0.0;
        if (productSnapshots == null) {
            return total;
        }
        for (ProductSnapshot snapshot : productSnapshots) {
            if (snapshot.getPrice() != null) {
                total += snapshot.getPrice();
            }
            if (snapshot.getSpecOptionSnapshots() == null) {
                continue;
            }
            for (SpecOptionSnapshot option : snapshot.getSpecOptionSnapshots()) {
                if (option.getExtraPrice() != null) {
                    total += option.getExtraPrice();
                }
            }
        }
        return total;
    }
}
