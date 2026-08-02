package food.delivery.catalog_ms.core.domain.entities;

import java.util.UUID;

public class RestaurantReference {

    private UUID ownerId;
    private UUID restaurantId;

    public RestaurantReference() {
    }

    public RestaurantReference(UUID ownerId, UUID restaurantId) {
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
