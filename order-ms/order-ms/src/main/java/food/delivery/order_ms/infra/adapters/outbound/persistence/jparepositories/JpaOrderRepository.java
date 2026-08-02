package food.delivery.order_ms.infra.adapters.outbound.persistence.jparepositories;

import food.delivery.order_ms.infra.adapters.outbound.persistence.jpaentities.JpaOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JpaOrderRepository extends JpaRepository<JpaOrder, UUID> {

    @Query("""
            SELECT o FROM JpaOrder o
            LEFT JOIN FETCH o.productSnapshots
            WHERE o.id = :id
            """)
    Optional<JpaOrder> findByIdWithDetails(@Param("id") UUID id);

    @Query("""
            SELECT DISTINCT o FROM JpaOrder o
            LEFT JOIN FETCH o.productSnapshots
            WHERE o.clientId = :clientId
            """)
    List<JpaOrder> findByClientIdWithDetails(@Param("clientId") UUID clientId);

    @Query("""
            SELECT DISTINCT o FROM JpaOrder o
            LEFT JOIN FETCH o.productSnapshots
            WHERE o.restaurantId = :restaurantId
              AND o.restaurantOwnerId = :ownerId
            """)
    List<JpaOrder> findByRestaurantIdAndOwnerIdWithDetails(
            @Param("restaurantId") UUID restaurantId,
            @Param("ownerId") UUID ownerId
    );
}
