package food.delivery.user_ms.infra.adapters.outbound.persistence.jparepositories;

import food.delivery.user_ms.infra.adapters.outbound.persistence.jpaentities.JpaAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaAddressRepository extends JpaRepository<JpaAddress, UUID> {
}
