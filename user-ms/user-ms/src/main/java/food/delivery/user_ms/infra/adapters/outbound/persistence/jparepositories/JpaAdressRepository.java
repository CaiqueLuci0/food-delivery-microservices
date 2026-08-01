package food.delivery.user_ms.infra.adapters.outbound.persistence.jparepositories;

import food.delivery.user_ms.infra.adapters.outbound.persistence.jpaentities.JpaAdress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaAdressRepository extends JpaRepository<JpaAdress, UUID> {
}
