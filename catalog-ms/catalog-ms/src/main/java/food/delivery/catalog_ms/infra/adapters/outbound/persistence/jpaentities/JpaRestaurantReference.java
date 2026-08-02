package food.delivery.catalog_ms.infra.adapters.outbound.persistence.jpaentities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "restaurant_reference")
@IdClass(JpaRestaurantReferenceId.class)
public class JpaRestaurantReference {

    @Id
    @Column(name = "owner_id", nullable = false)
    private UUID ownerId;

    @Id
    @Column(name = "restaurant_id", nullable = false)
    private UUID restaurantId;

    public JpaRestaurantReference() {
    }

    public JpaRestaurantReference(UUID ownerId, UUID restaurantId) {
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
}
