package food.delivery.user_ms.infra.adapters.outbound.persistence.adapters;

import food.delivery.user_ms.core.application.ports.out.UserRepositoryOutputPort;
import food.delivery.user_ms.core.domain.entities.User;
import food.delivery.user_ms.infra.adapters.outbound.persistence.jpaentities.JpaUser;
import food.delivery.user_ms.infra.adapters.outbound.persistence.jparepositories.JpaUserRepository;
import food.delivery.user_ms.infra.adapters.outbound.persistence.mappers.UserPersistenceMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class UserRepositoryOutputPortAdapter implements UserRepositoryOutputPort {

    private final JpaUserRepository jpaUserRepository;

    public UserRepositoryOutputPortAdapter(JpaUserRepository jpaUserRepository) {
        this.jpaUserRepository = jpaUserRepository;
    }

    @Override
    public User save(User user) {
        JpaUser saved = jpaUserRepository.save(UserPersistenceMapper.toJpa(user));
        return UserPersistenceMapper.toDomain(saved);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return jpaUserRepository.findById(id).map(UserPersistenceMapper::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return jpaUserRepository.findByEmail(email).map(UserPersistenceMapper::toDomain);
    }

    @Override
    public List<User> findAll() {
        return jpaUserRepository.findAll().stream()
                .map(UserPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public void delete(User user) {
        jpaUserRepository.deleteById(user.getId());
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaUserRepository.existsByEmail(email);
    }
}
