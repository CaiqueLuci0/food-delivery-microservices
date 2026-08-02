package food.delivery.catalog_ms.infra.adapters.outbound.persistence.adapters;

import food.delivery.catalog_ms.core.application.ports.out.RestaurantReferenceRepositoryOutputPort;
import food.delivery.catalog_ms.core.domain.entities.RestaurantReference;
import food.delivery.catalog_ms.infra.adapters.outbound.persistence.jparepositories.JpaRestaurantReferenceRepository;
import food.delivery.catalog_ms.infra.adapters.outbound.persistence.mappers.RestaurantReferencePersistenceMapper;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class RestaurantReferenceRepositoryOutputPortAdapter implements RestaurantReferenceRepositoryOutputPort {

    private final JpaRestaurantReferenceRepository jpaRestaurantReferenceRepository;

    public RestaurantReferenceRepositoryOutputPortAdapter(
            JpaRestaurantReferenceRepository jpaRestaurantReferenceRepository
    ) {
        this.jpaRestaurantReferenceRepository = jpaRestaurantReferenceRepository;
    }

    @Override
    public RestaurantReference save(RestaurantReference restaurantReference) {
        return RestaurantReferencePersistenceMapper.toDomain(
                jpaRestaurantReferenceRepository.save(
                        RestaurantReferencePersistenceMapper.toJpa(restaurantReference)
                )
        );
    }

    @Override
    public Optional<RestaurantReference> findByOwnerId(UUID ownerId) {
        return jpaRestaurantReferenceRepository.findByOwnerId(ownerId)
                .map(RestaurantReferencePersistenceMapper::toDomain);
    }

    @Override
    public Optional<RestaurantReference> findByRestaurantId(UUID restaurantId) {
        return jpaRestaurantReferenceRepository.findByRestaurantId(restaurantId)
                .map(RestaurantReferencePersistenceMapper::toDomain);
    }

    @Override
    public boolean existsByOwnerIdAndRestaurantId(UUID ownerId, UUID restaurantId) {
        return jpaRestaurantReferenceRepository.existsByOwnerIdAndRestaurantId(ownerId, restaurantId);
    }

    @Override
    public void deleteByRestaurantId(UUID restaurantId) {
        jpaRestaurantReferenceRepository.deleteByRestaurantId(restaurantId);
    }
}
