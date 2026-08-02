package food.delivery.catalog_ms.infra.adapters.outbound.persistence.jparepositories;

import food.delivery.catalog_ms.infra.adapters.outbound.persistence.jpaentities.JpaSpecOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaSpecOptionRepository extends JpaRepository<JpaSpecOption, UUID> {
}
