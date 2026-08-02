package food.delivery.restaurant_ms.infra.adapters.outbound.persistence.adapters;

import food.delivery.restaurant_ms.core.application.ports.out.RestaurantRepositoryOutputPort;
import food.delivery.restaurant_ms.core.domain.entities.Restaurant;
import food.delivery.restaurant_ms.infra.adapters.outbound.persistence.jpaentities.JpaRestaurant;
import food.delivery.restaurant_ms.infra.adapters.outbound.persistence.jparepositories.JpaRestaurantRepository;
import food.delivery.restaurant_ms.infra.adapters.outbound.persistence.mappers.RestaurantPersistenceMapper;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class RestaurantRepositoryOutputPortAdapter implements RestaurantRepositoryOutputPort {

    private final JpaRestaurantRepository jpaRestaurantRepository;

    public RestaurantRepositoryOutputPortAdapter(JpaRestaurantRepository jpaRestaurantRepository) {
        this.jpaRestaurantRepository = jpaRestaurantRepository;
    }

    @Override
    public Restaurant save(Restaurant restaurant) {
        JpaRestaurant jpaRestaurant = RestaurantPersistenceMapper.toJpa(restaurant);
        return RestaurantPersistenceMapper.toDomain(jpaRestaurantRepository.save(jpaRestaurant));
    }

    @Override
    public Optional<Restaurant> findById(UUID id) {
        return jpaRestaurantRepository.findById(id).map(RestaurantPersistenceMapper::toDomain);
    }

    @Override
    public Optional<Restaurant> findByOwnerId(UUID ownerId) {
        return jpaRestaurantRepository.findByOwnerId(ownerId).map(RestaurantPersistenceMapper::toDomain);
    }

    @Override
    public List<Restaurant> findAll() {
        return jpaRestaurantRepository.findAll().stream()
                .map(RestaurantPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public List<Restaurant> searchByNameOrDescription(String search) {
        return jpaRestaurantRepository.searchByNameOrDescription(search).stream()
                .map(RestaurantPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public List<Restaurant> findWithinRadius(
            BigDecimal latitude,
            BigDecimal longitude,
            double radiusMeters,
            String search
    ) {
        return jpaRestaurantRepository.findWithinRadius(latitude, longitude, radiusMeters, search).stream()
                .map(RestaurantPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public void delete(Restaurant restaurant) {
        jpaRestaurantRepository.deleteById(restaurant.getId());
    }

    @Override
    public boolean existsByOwnerId(UUID ownerId) {
        return jpaRestaurantRepository.existsByOwnerId(ownerId);
    }
}
