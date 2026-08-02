package food.delivery.catalog_ms.infra.adapters.outbound.persistence.jpaentities;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class JpaRestaurantReferenceId implements Serializable {

    private UUID ownerId;
    private UUID restaurantId;

    public JpaRestaurantReferenceId() {
    }

    public JpaRestaurantReferenceId(UUID ownerId, UUID restaurantId) {
        this.ownerId = ownerId;
        this.restaurantId = restaurantId;
    }

    public UUID getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(UUID ownerId) {
        this.ownerId = ownerId;
    }

    public UUID getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(UUID restaurantId) {
        this.restaurantId = restaurantId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof JpaRestaurantReferenceId that)) {
            return false;
        }
        return Objects.equals(ownerId, that.ownerId) && Objects.equals(restaurantId, that.restaurantId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ownerId, restaurantId);
    }
}
