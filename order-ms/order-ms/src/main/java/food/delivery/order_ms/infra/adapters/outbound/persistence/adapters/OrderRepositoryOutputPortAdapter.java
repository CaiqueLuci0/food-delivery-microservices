package food.delivery.order_ms.infra.adapters.outbound.persistence.adapters;

import food.delivery.order_ms.core.application.ports.out.OrderRepositoryOutputPort;
import food.delivery.order_ms.core.domain.entities.Order;
import food.delivery.order_ms.infra.adapters.outbound.persistence.jpaentities.JpaOrder;
import food.delivery.order_ms.infra.adapters.outbound.persistence.jparepositories.JpaOrderRepository;
import food.delivery.order_ms.infra.adapters.outbound.persistence.mappers.OrderPersistenceMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class OrderRepositoryOutputPortAdapter implements OrderRepositoryOutputPort {

    private final JpaOrderRepository jpaOrderRepository;

    public OrderRepositoryOutputPortAdapter(JpaOrderRepository jpaOrderRepository) {
        this.jpaOrderRepository = jpaOrderRepository;
    }

    @Override
    public Order save(Order order) {
        JpaOrder jpaOrder;
        if (order.getId() != null) {
            jpaOrder = jpaOrderRepository.findByIdWithDetails(order.getId())
                    .orElseGet(JpaOrder::new);
            OrderPersistenceMapper.mergeScalars(order, jpaOrder);
        } else {
            jpaOrder = OrderPersistenceMapper.toJpa(order);
        }
        return OrderPersistenceMapper.toDomain(jpaOrderRepository.save(jpaOrder));
    }

    @Override
    public Order saveReplacingSnapshots(Order order) {
        JpaOrder jpaOrder = jpaOrderRepository.findByIdWithDetails(order.getId())
                .orElseGet(JpaOrder::new);
        OrderPersistenceMapper.mergeScalars(order, jpaOrder);
        jpaOrder.getProductSnapshots().clear();
        jpaOrderRepository.flush();
        OrderPersistenceMapper.replaceSnapshots(order, jpaOrder);
        return OrderPersistenceMapper.toDomain(jpaOrderRepository.save(jpaOrder));
    }

    @Override
    public Optional<Order> findById(UUID id) {
        return jpaOrderRepository.findByIdWithDetails(id).map(OrderPersistenceMapper::toDomain);
    }

    @Override
    public List<Order> findByClientId(UUID clientId) {
        return jpaOrderRepository.findByClientIdWithDetails(clientId).stream()
                .map(OrderPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public List<Order> findByRestaurantIdAndOwnerId(UUID restaurantId, UUID ownerId) {
        return jpaOrderRepository.findByRestaurantIdAndOwnerIdWithDetails(restaurantId, ownerId).stream()
                .map(OrderPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public void delete(Order order) {
        jpaOrderRepository.deleteById(order.getId());
    }
}
