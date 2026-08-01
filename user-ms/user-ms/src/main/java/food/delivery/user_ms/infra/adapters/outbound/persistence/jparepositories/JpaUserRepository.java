package food.delivery.user_ms.infra.adapters.outbound.persistence.jparepositories;

import food.delivery.user_ms.infra.adapters.outbound.persistence.jpaentities.JpaUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface JpaUserRepository extends JpaRepository<JpaUser, UUID> {
    Optional<JpaUser> findByEmail(String email);
    boolean existsByEmail(String email);
}
