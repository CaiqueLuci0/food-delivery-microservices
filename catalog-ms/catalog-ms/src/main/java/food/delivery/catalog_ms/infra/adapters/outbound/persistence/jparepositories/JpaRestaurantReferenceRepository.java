package food.delivery.catalog_ms.infra.adapters.outbound.persistence.jparepositories;

import food.delivery.catalog_ms.infra.adapters.outbound.persistence.jpaentities.JpaRestaurantReference;
import food.delivery.catalog_ms.infra.adapters.outbound.persistence.jpaentities.JpaRestaurantReferenceId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface JpaRestaurantReferenceRepository
        extends JpaRepository<JpaRestaurantReference, JpaRestaurantReferenceId> {

    Optional<JpaRestaurantReference> findByOwnerId(UUID ownerId);

    Optional<JpaRestaurantReference> findByRestaurantId(UUID restaurantId);

    boolean existsByOwnerIdAndRestaurantId(UUID ownerId, UUID restaurantId);

    void deleteByRestaurantId(UUID restaurantId);
}
