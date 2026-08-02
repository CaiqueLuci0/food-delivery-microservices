package food.delivery.order_ms.infra.adapters.outbound.persistence.adapters;

import food.delivery.order_ms.core.application.ports.out.UserReferenceRepositoryOutputPort;
import food.delivery.order_ms.core.domain.entities.UserReference;
import food.delivery.order_ms.infra.adapters.outbound.persistence.jparepositories.JpaUserReferenceRepository;
import food.delivery.order_ms.infra.adapters.outbound.persistence.mappers.UserReferencePersistenceMapper;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class UserReferenceRepositoryOutputPortAdapter implements UserReferenceRepositoryOutputPort {

    private final JpaUserReferenceRepository jpaUserReferenceRepository;

    public UserReferenceRepositoryOutputPortAdapter(JpaUserReferenceRepository jpaUserReferenceRepository) {
        this.jpaUserReferenceRepository = jpaUserReferenceRepository;
    }

    @Override
    public UserReference save(UserReference userReference) {
        return UserReferencePersistenceMapper.toDomain(
                jpaUserReferenceRepository.save(UserReferencePersistenceMapper.toJpa(userReference))
        );
    }

    @Override
    public Optional<UserReference> findById(UUID id) {
        return jpaUserReferenceRepository.findById(id).map(UserReferencePersistenceMapper::toDomain);
    }

    @Override
    public boolean existsById(UUID id) {
        return jpaUserReferenceRepository.existsById(id);
    }

    @Override
    public void delete(UserReference userReference) {
        jpaUserReferenceRepository.deleteById(userReference.getId());
    }
}
