package food.delivery.order_ms.infra.adapters.outbound.persistence.mappers;

import food.delivery.order_ms.core.domain.entities.Order;
import food.delivery.order_ms.core.domain.entities.ProductSnapshot;
import food.delivery.order_ms.core.domain.entities.SpecOptionSnapshot;
import food.delivery.order_ms.infra.adapters.outbound.persistence.jpaentities.JpaOrder;
import food.delivery.order_ms.infra.adapters.outbound.persistence.jpaentities.JpaProductSnapshot;
import food.delivery.order_ms.infra.adapters.outbound.persistence.jpaentities.JpaSpecOptionSnapshot;

import java.util.ArrayList;
import java.util.List;

public final class OrderPersistenceMapper {

    private OrderPersistenceMapper() {
    }

    public static JpaOrder toJpa(Order order) {
        if (order == null) {
            return null;
        }
        JpaOrder jpaOrder = new JpaOrder();
        mergeScalars(order, jpaOrder);
        List<JpaProductSnapshot> snapshots = new ArrayList<>();
        if (order.getProductSnapshots() != null) {
            for (ProductSnapshot snapshot : order.getProductSnapshots()) {
                snapshots.add(toJpaProductSnapshot(snapshot, jpaOrder));
            }
        }
        jpaOrder.setProductSnapshots(snapshots);
        return jpaOrder;
    }

    public static void mergeScalars(Order order, JpaOrder jpaOrder) {
        jpaOrder.setId(order.getId());
        jpaOrder.setStatus(order.getStatus());
        jpaOrder.setPaymentStatus(order.getPaymentStatus());
        jpaOrder.setScore(order.getScore());
        jpaOrder.setRestaurantId(order.getRestaurantId());
        jpaOrder.setRestaurantOwnerId(order.getRestaurantOwnerId());
        jpaOrder.setClientId(order.getClientId());
    }

    public static void replaceSnapshots(Order order, JpaOrder jpaOrder) {
        if (order.getProductSnapshots() == null) {
            return;
        }
        for (ProductSnapshot snapshot : order.getProductSnapshots()) {
            jpaOrder.getProductSnapshots().add(toNewJpaProductSnapshot(snapshot, jpaOrder));
        }
    }

    public static Order toDomain(JpaOrder jpaOrder) {
        if (jpaOrder == null) {
            return null;
        }
        Order order = new Order();
        order.setId(jpaOrder.getId());
        order.setStatus(jpaOrder.getStatus());
        order.setPaymentStatus(jpaOrder.getPaymentStatus());
        order.setScore(jpaOrder.getScore());
        order.setRestaurantId(jpaOrder.getRestaurantId());
        order.setRestaurantOwnerId(jpaOrder.getRestaurantOwnerId());
        order.setClientId(jpaOrder.getClientId());

        List<ProductSnapshot> snapshots = new ArrayList<>();
        if (jpaOrder.getProductSnapshots() != null) {
            for (JpaProductSnapshot jpaSnapshot : jpaOrder.getProductSnapshots()) {
                snapshots.add(toDomainProductSnapshot(jpaSnapshot, order));
            }
        }
        order.setProductSnapshots(snapshots);
        return order;
    }

    private static JpaProductSnapshot toJpaProductSnapshot(ProductSnapshot snapshot, JpaOrder jpaOrder) {
        JpaProductSnapshot jpaSnapshot = new JpaProductSnapshot();
        jpaSnapshot.setId(snapshot.getId());
        jpaSnapshot.setName(snapshot.getName());
        jpaSnapshot.setPrice(snapshot.getPrice());
        jpaSnapshot.setDescription(snapshot.getDescription());
        jpaSnapshot.setOrder(jpaOrder);

        List<JpaSpecOptionSnapshot> options = new ArrayList<>();
        if (snapshot.getSpecOptionSnapshots() != null) {
            for (SpecOptionSnapshot option : snapshot.getSpecOptionSnapshots()) {
                options.add(toJpaSpecOptionSnapshot(option, jpaSnapshot));
            }
        }
        jpaSnapshot.setSpecOptionSnapshots(options);
        return jpaSnapshot;
    }

    private static JpaProductSnapshot toNewJpaProductSnapshot(ProductSnapshot snapshot, JpaOrder jpaOrder) {
        JpaProductSnapshot jpaSnapshot = new JpaProductSnapshot();
        jpaSnapshot.setId(null);
        jpaSnapshot.setName(snapshot.getName());
        jpaSnapshot.setPrice(snapshot.getPrice());
        jpaSnapshot.setDescription(snapshot.getDescription());
        jpaSnapshot.setOrder(jpaOrder);

        List<JpaSpecOptionSnapshot> options = new ArrayList<>();
        if (snapshot.getSpecOptionSnapshots() != null) {
            for (SpecOptionSnapshot option : snapshot.getSpecOptionSnapshots()) {
                options.add(toNewJpaSpecOptionSnapshot(option, jpaSnapshot));
            }
        }
        jpaSnapshot.setSpecOptionSnapshots(options);
        return jpaSnapshot;
    }

    private static JpaSpecOptionSnapshot toJpaSpecOptionSnapshot(
            SpecOptionSnapshot option,
            JpaProductSnapshot jpaSnapshot
    ) {
        JpaSpecOptionSnapshot jpaOption = new JpaSpecOptionSnapshot();
        jpaOption.setId(option.getId());
        jpaOption.setName(option.getName());
        jpaOption.setDescription(option.getDescription());
        jpaOption.setExtraPrice(option.getExtraPrice());
        jpaOption.setProductSnapshot(jpaSnapshot);
        return jpaOption;
    }

    private static JpaSpecOptionSnapshot toNewJpaSpecOptionSnapshot(
            SpecOptionSnapshot option,
            JpaProductSnapshot jpaSnapshot
    ) {
        JpaSpecOptionSnapshot jpaOption = new JpaSpecOptionSnapshot();
        jpaOption.setId(null);
        jpaOption.setName(option.getName());
        jpaOption.setDescription(option.getDescription());
        jpaOption.setExtraPrice(option.getExtraPrice());
        jpaOption.setProductSnapshot(jpaSnapshot);
        return jpaOption;
    }

    private static ProductSnapshot toDomainProductSnapshot(JpaProductSnapshot jpaSnapshot, Order order) {
        ProductSnapshot snapshot = new ProductSnapshot();
        snapshot.setId(jpaSnapshot.getId());
        snapshot.setName(jpaSnapshot.getName());
        snapshot.setPrice(jpaSnapshot.getPrice());
        snapshot.setDescription(jpaSnapshot.getDescription());
        snapshot.setOrder(order);

        List<SpecOptionSnapshot> options = new ArrayList<>();
        if (jpaSnapshot.getSpecOptionSnapshots() != null) {
            for (JpaSpecOptionSnapshot jpaOption : jpaSnapshot.getSpecOptionSnapshots()) {
                SpecOptionSnapshot option = new SpecOptionSnapshot();
                option.setId(jpaOption.getId());
                option.setName(jpaOption.getName());
                option.setDescription(jpaOption.getDescription());
                option.setExtraPrice(jpaOption.getExtraPrice());
                option.setProductSnapshot(snapshot);
                options.add(option);
            }
        }
        snapshot.setSpecOptionSnapshots(options);
        return snapshot;
    }
}
