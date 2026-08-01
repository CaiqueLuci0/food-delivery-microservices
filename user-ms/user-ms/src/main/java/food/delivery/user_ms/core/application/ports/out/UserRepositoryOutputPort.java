package food.delivery.user_ms.core.application.ports.out;

import food.delivery.user_ms.core.domain.entities.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepositoryOutputPort {
    User save(User user);
    Optional<User> findById(UUID id);
    Optional<User> findByEmail(String email);
    List<User> findAll();
    void delete(User user);
    boolean existsByEmail(String email);
}
