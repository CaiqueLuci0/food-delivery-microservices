package food.delivery.restaurant_ms.infra.adapters.outbound.persistence.jparepositories;

import food.delivery.restaurant_ms.infra.adapters.outbound.persistence.jpaentities.JpaRestaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
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

    @Query(value = """
            SELECT r.* FROM restaurant r
            INNER JOIN address a ON a.restaurant_id = r.id
            WHERE a.latitude IS NOT NULL
              AND a.longitude IS NOT NULL
              AND ST_Distance_Sphere(
                    POINT(a.longitude, a.latitude),
                    POINT(:longitude, :latitude)
                  ) <= :radiusMeters
              AND (
                    :search IS NULL
                    OR :search = ''
                    OR LOWER(r.name) LIKE LOWER(CONCAT('%', :search, '%'))
                    OR LOWER(r.description) LIKE LOWER(CONCAT('%', :search, '%'))
                  )
            """, nativeQuery = true)
    List<JpaRestaurant> findWithinRadius(
            @Param("latitude") BigDecimal latitude,
            @Param("longitude") BigDecimal longitude,
            @Param("radiusMeters") double radiusMeters,
            @Param("search") String search
    );
}
