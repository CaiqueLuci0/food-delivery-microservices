package food.delivery.restaurant_ms.infra.adapters.outbound.persistence.jparepositories;

import food.delivery.restaurant_ms.infra.adapters.outbound.persistence.jpaentities.JpaRestaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JpaRestaurantRepository extends JpaRepository<JpaRestaurant, UUID> {

    Optional<JpaRestaurant> findByOwnerId(UUID ownerId);

    boolean existsByOwnerId(UUID ownerId);

    @Query("""
            SELECT r FROM JpaRestaurant r
            WHERE LOWER(r.name) LIKE LOWER(CONCAT('%', :search, '%'))
               OR LOWER(r.description) LIKE LOWER(CONCAT('%', :search, '%'))
            """)
    List<JpaRestaurant> searchByNameOrDescription(@Param("search") String search);
}
