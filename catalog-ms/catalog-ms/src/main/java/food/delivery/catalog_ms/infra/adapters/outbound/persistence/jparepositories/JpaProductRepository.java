package food.delivery.catalog_ms.infra.adapters.outbound.persistence.jparepositories;

import food.delivery.catalog_ms.infra.adapters.outbound.persistence.jpaentities.JpaProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JpaProductRepository extends JpaRepository<JpaProduct, UUID> {

    @Query("""
            SELECT p FROM JpaProduct p
            LEFT JOIN FETCH p.specifications
            WHERE p.id = :id
            """)
    Optional<JpaProduct> findByIdWithDetails(@Param("id") UUID id);

    @Query("""
            SELECT DISTINCT p FROM JpaProduct p
            LEFT JOIN FETCH p.specifications
            WHERE p.restaurantId = :restaurantId
            """)
    List<JpaProduct> findByRestaurantIdWithDetails(@Param("restaurantId") UUID restaurantId);

    @Query("""
            SELECT DISTINCT p FROM JpaProduct p
            LEFT JOIN FETCH p.specifications
            WHERE p.restaurantId = :restaurantId
              AND (LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%'))
                   OR LOWER(p.description) LIKE LOWER(CONCAT('%', :search, '%')))
            """)
    List<JpaProduct> findByRestaurantIdAndSearch(
            @Param("restaurantId") UUID restaurantId,
            @Param("search") String search
    );
}
