package food.delivery.order_ms.core.domain.entities;

import java.util.List;
import java.util.UUID;

public class CatalogResolution {

    private final UUID restaurantId;
    private final UUID restaurantOwnerId;
    private final List<ProductSnapshot> productSnapshots;

    public CatalogResolution(UUID restaurantId, UUID restaurantOwnerId, List<ProductSnapshot> productSnapshots) {
        this.restaurantId = restaurantId;
        this.restaurantOwnerId = restaurantOwnerId;
        this.productSnapshots = productSnapshots != null
                ? List.copyOf(productSnapshots)
                : List.of();
    }

    public UUID getRestaurantId() {
        return restaurantId;
    }

    public UUID getRestaurantOwnerId() {
        return restaurantOwnerId;
    }

    public List<ProductSnapshot> getProductSnapshots() {
        return productSnapshots;
    }
}
