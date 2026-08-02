package food.delivery.catalog_ms.infra.adapters.outbound.persistence.mappers;

import food.delivery.catalog_ms.core.domain.entities.RestaurantReference;
import food.delivery.catalog_ms.infra.adapters.outbound.persistence.jpaentities.JpaRestaurantReference;

public final class RestaurantReferencePersistenceMapper {

    private RestaurantReferencePersistenceMapper() {
    }

    public static JpaRestaurantReference toJpa(RestaurantReference restaurantReference) {
        if (restaurantReference == null) {
            return null;
        }
        return new JpaRestaurantReference(
                restaurantReference.getOwnerId(),
                restaurantReference.getRestaurantId()
        );
    }

    public static RestaurantReference toDomain(JpaRestaurantReference jpaRestaurantReference) {
        if (jpaRestaurantReference == null) {
            return null;
        }
        return new RestaurantReference(
                jpaRestaurantReference.getOwnerId(),
                jpaRestaurantReference.getRestaurantId()
        );
    }
}
