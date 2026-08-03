package food.delivery.order_ms.core.application.usecases;

import food.delivery.order_ms.core.application.ports.in.OrderUseCaseInputPort;
import food.delivery.order_ms.core.application.ports.out.CatalogResolveOutputPort;
import food.delivery.order_ms.core.application.ports.out.OrderCreatedEventOutputPort;
import food.delivery.order_ms.core.application.ports.out.OrderDeletedEventOutputPort;
import food.delivery.order_ms.core.application.ports.out.OrderRepositoryOutputPort;
import food.delivery.order_ms.core.application.ports.out.OrderStatusPushOutputPort;
import food.delivery.order_ms.core.application.ports.out.UserReferenceRepositoryOutputPort;
import food.delivery.order_ms.core.domain.entities.CatalogResolution;
import food.delivery.order_ms.core.domain.entities.Order;
import food.delivery.order_ms.core.domain.enums.ConstMessagesEnum;
import food.delivery.order_ms.core.domain.enums.OrderStatus;
import food.delivery.order_ms.core.domain.enums.PaymentStatus;
import food.delivery.order_ms.core.domain.exceptions.ConflictException;
import food.delivery.order_ms.core.domain.exceptions.ForbiddenException;
import food.delivery.order_ms.core.domain.exceptions.NotFoundException;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class OrderUseCase implements OrderUseCaseInputPort {

    private static final Set<OrderStatus> CLIENT_CANCELABLE = EnumSet.of(
            OrderStatus.EM_CADASTRAMENTO,
            OrderStatus.AGUARDANDO_PAGAMENTO
    );

    private static final Set<OrderStatus> OWNER_CANCELABLE = EnumSet.of(
            OrderStatus.AGUARDANDO_RESTAURANTE,
            OrderStatus.PREPARANDO,
            OrderStatus.SAIU_PARA_ENTREGA,
            OrderStatus.ENTREGADOR_NO_LOCAL,
            OrderStatus.ENTREGUE
    );

    private static final Set<OrderStatus> OWNER_UPDATABLE = EnumSet.of(
            OrderStatus.AGUARDANDO_RESTAURANTE,
            OrderStatus.PREPARANDO,
            OrderStatus.SAIU_PARA_ENTREGA,
            OrderStatus.ENTREGADOR_NO_LOCAL,
            OrderStatus.ENTREGUE
    );

    private final OrderRepositoryOutputPort orderRepositoryOutputPort;
    private final UserReferenceRepositoryOutputPort userReferenceRepositoryOutputPort;
    private final CatalogResolveOutputPort catalogResolveOutputPort;
    private final OrderCreatedEventOutputPort orderCreatedEventOutputPort;
    private final OrderDeletedEventOutputPort orderDeletedEventOutputPort;
    private final OrderStatusPushOutputPort orderStatusPushOutputPort;

    public OrderUseCase(
            OrderRepositoryOutputPort orderRepositoryOutputPort,
            UserReferenceRepositoryOutputPort userReferenceRepositoryOutputPort,
            CatalogResolveOutputPort catalogResolveOutputPort,
            OrderCreatedEventOutputPort orderCreatedEventOutputPort,
            OrderDeletedEventOutputPort orderDeletedEventOutputPort,
            OrderStatusPushOutputPort orderStatusPushOutputPort
    ) {
        this.orderRepositoryOutputPort = orderRepositoryOutputPort;
        this.userReferenceRepositoryOutputPort = userReferenceRepositoryOutputPort;
        this.catalogResolveOutputPort = catalogResolveOutputPort;
        this.orderCreatedEventOutputPort = orderCreatedEventOutputPort;
        this.orderDeletedEventOutputPort = orderDeletedEventOutputPort;
        this.orderStatusPushOutputPort = orderStatusPushOutputPort;
    }

    @Override
    public Order create(UUID clientId, List<OrderItem> items, String bearerToken) {
        requireUserReference(clientId);
        CatalogResolution resolution = resolveItems(items, bearerToken);

        Order order = Order.create(clientId);
        order.applyCatalogResolution(resolution);

        return orderRepositoryOutputPort.save(order);
    }

    @Override
    public Order update(UUID clientId, UUID orderId, List<OrderItem> items, String bearerToken) {
        Order order = requireOrder(orderId);
        requireClient(order, clientId);
        if (order.getStatus() != OrderStatus.EM_CADASTRAMENTO) {
            throw new ConflictException(ConstMessagesEnum.INVALID_ORDER_STATUS.getMessage());
        }

        CatalogResolution resolution = resolveItems(items, bearerToken);
        order.applyCatalogResolution(resolution);

        return orderRepositoryOutputPort.saveReplacingSnapshots(order);
    }

    @Override
    public Order finalize(UUID clientId, UUID orderId) {
        Order order = requireOrder(orderId);
        requireClient(order, clientId);
        if (order.getStatus() != OrderStatus.EM_CADASTRAMENTO) {
            throw new ConflictException(ConstMessagesEnum.INVALID_ORDER_STATUS.getMessage());
        }

        order.setStatus(OrderStatus.AGUARDANDO_PAGAMENTO);
        Order saved = orderRepositoryOutputPort.save(order);

        double price = saved.computePrice();
        orderCreatedEventOutputPort.publish(
                UUID.randomUUID(),
                saved.getId(),
                saved.getClientId(),
                "AGUARDANDO",
                price
        );
        orderStatusPushOutputPort.push(saved);
        return saved;
    }

    @Override
    public Order updateStatus(UUID ownerId, UUID orderId, OrderStatus status) {
        Order order = requireOrder(orderId);
        requireOwner(order, ownerId);
        if (order.getPaymentStatus() != PaymentStatus.PAGO) {
            throw new ConflictException(ConstMessagesEnum.ORDER_NOT_PAYABLE.getMessage());
        }
        if (status == null || !OWNER_UPDATABLE.contains(status)) {
            throw new ConflictException(ConstMessagesEnum.INVALID_ORDER_STATUS.getMessage());
        }

        order.setStatus(status);
        Order saved = orderRepositoryOutputPort.save(order);
        orderStatusPushOutputPort.push(saved);
        return saved;
    }

    @Override
    public Order cancel(UUID userId, UUID orderId) {
        Order order = requireOrder(orderId);
        boolean isClient = order.belongsTo(userId);
        boolean isOwner = order.isOwnedBy(userId);

        if (isClient && CLIENT_CANCELABLE.contains(order.getStatus())) {
            order.setStatus(OrderStatus.CANCELADO);
            Order saved = orderRepositoryOutputPort.save(order);
            orderDeletedEventOutputPort.publish(saved.getId());
            orderStatusPushOutputPort.push(saved);
            return saved;
        }
        if (isOwner && OWNER_CANCELABLE.contains(order.getStatus())) {
            order.setStatus(OrderStatus.CANCELADO);
            Order saved = orderRepositoryOutputPort.save(order);
            orderDeletedEventOutputPort.publish(saved.getId());
            orderStatusPushOutputPort.push(saved);
            return saved;
        }
        if (!isClient && !isOwner) {
            throw new ForbiddenException(ConstMessagesEnum.ACCESS_DENIED.getMessage());
        }
        throw new ConflictException(ConstMessagesEnum.INVALID_ORDER_STATUS.getMessage());
    }

    @Override
    public Order findById(UUID userId, UUID id) {
        Order order = requireOrder(id);
        if (!order.belongsTo(userId) && !order.isOwnedBy(userId)) {
            throw new ForbiddenException(ConstMessagesEnum.ACCESS_DENIED.getMessage());
        }
        return order;
    }

    @Override
    public List<Order> findAll(UUID userId, UUID restaurantId) {
        if (restaurantId == null) {
            return orderRepositoryOutputPort.findByClientId(userId);
        }
        return orderRepositoryOutputPort.findByRestaurantIdAndOwnerId(restaurantId, userId);
    }

    private void requireUserReference(UUID clientId) {
        if (clientId == null || !userReferenceRepositoryOutputPort.existsById(clientId)) {
            throw new NotFoundException(ConstMessagesEnum.USER_NOT_FOUND.getMessage());
        }
    }

    private Order requireOrder(UUID orderId) {
        return orderRepositoryOutputPort.findById(orderId)
                .orElseThrow(() -> new NotFoundException(ConstMessagesEnum.NOT_FOUND.getMessage()));
    }

    private void requireClient(Order order, UUID clientId) {
        if (!order.belongsTo(clientId)) {
            throw new ForbiddenException(ConstMessagesEnum.ACCESS_DENIED.getMessage());
        }
    }

    private void requireOwner(Order order, UUID ownerId) {
        if (!order.isOwnedBy(ownerId)) {
            throw new ForbiddenException(ConstMessagesEnum.ACCESS_DENIED.getMessage());
        }
    }

    private CatalogResolution resolveItems(List<OrderItem> items, String bearerToken) {
        if (items == null || items.isEmpty()) {
            throw new ConflictException(ConstMessagesEnum.INVALID_REQUEST.getMessage());
        }
        List<CatalogResolveOutputPort.ResolveItem> resolveItems = items.stream()
                .map(item -> new CatalogResolveOutputPort.ResolveItem(
                        item.productId(),
                        item.specOptionIds() != null ? item.specOptionIds() : List.of()
                ))
                .toList();
        return catalogResolveOutputPort.resolve(resolveItems, bearerToken);
    }
}
