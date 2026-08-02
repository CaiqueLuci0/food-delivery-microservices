package food.delivery.restaurant_ms.infra.adapters.outbound.persistence.jparepositories;

import food.delivery.restaurant_ms.infra.adapters.outbound.persistence.jpaentities.JpaUserReference;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaUserReferenceRepository extends JpaRepository<JpaUserReference, UUID> {
}
