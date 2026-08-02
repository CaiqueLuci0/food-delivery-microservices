package food.delivery.order_ms.infra.adapters.inbound.web.facade;

import food.delivery.order_ms.core.application.ports.in.OrderUseCaseInputPort;
import food.delivery.order_ms.core.domain.entities.Order;
import food.delivery.order_ms.infra.adapters.inbound.web.presenter.dto.ordercontroller.create.OrderCreateRequestDto;
import food.delivery.order_ms.infra.adapters.inbound.web.presenter.dto.ordercontroller.get.OrderResponseDto;
import food.delivery.order_ms.infra.adapters.inbound.web.presenter.dto.ordercontroller.get.OrderResponseMapper;
import food.delivery.order_ms.infra.adapters.inbound.web.presenter.dto.ordercontroller.update.OrderStatusUpdateRequestDto;
import food.delivery.order_ms.infra.adapters.inbound.web.presenter.dto.ordercontroller.update.OrderUpdateRequestDto;
import food.delivery.order_ms.infra.adapters.inbound.web.security.AuthenticatedUser;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Component
public class OrderFacade {

    private final OrderUseCaseInputPort orderUseCase;

    public OrderFacade(OrderUseCaseInputPort orderUseCase) {
        this.orderUseCase = orderUseCase;
    }

    @Transactional
    public OrderResponseDto create(OrderCreateRequestDto request, String bearerToken) {
        List<OrderUseCaseInputPort.OrderItem> items = request.getItems().stream()
                .map(item -> new OrderUseCaseInputPort.OrderItem(item.getProductId(), item.getSpecOptionIds()))
                .toList();
        Order created = orderUseCase.create(AuthenticatedUser.requireId(), items, bearerToken);
        return OrderResponseMapper.toResponse(created);
    }

    @Transactional
    public OrderResponseDto update(UUID id, OrderUpdateRequestDto request, String bearerToken) {
        List<OrderUseCaseInputPort.OrderItem> items = request.getItems().stream()
                .map(item -> new OrderUseCaseInputPort.OrderItem(item.getProductId(), item.getSpecOptionIds()))
                .toList();
        Order updated = orderUseCase.update(AuthenticatedUser.requireId(), id, items, bearerToken);
        return OrderResponseMapper.toResponse(updated);
    }

    @Transactional
    public OrderResponseDto finalizeOrder(UUID id) {
        Order finalized = orderUseCase.finalize(AuthenticatedUser.requireId(), id);
        return OrderResponseMapper.toResponse(finalized);
    }

    @Transactional
    public OrderResponseDto updateStatus(UUID id, OrderStatusUpdateRequestDto request) {
        Order updated = orderUseCase.updateStatus(
                AuthenticatedUser.requireId(),
                id,
                request.getStatus()
        );
        return OrderResponseMapper.toResponse(updated);
    }

    @Transactional
    public OrderResponseDto cancel(UUID id) {
        Order cancelled = orderUseCase.cancel(AuthenticatedUser.requireId(), id);
        return OrderResponseMapper.toResponse(cancelled);
    }

    public OrderResponseDto findById(UUID id) {
        return OrderResponseMapper.toResponse(orderUseCase.findById(AuthenticatedUser.requireId(), id));
    }

    public List<OrderResponseDto> findAll(UUID restaurantId) {
        return OrderResponseMapper.toResponseList(
                orderUseCase.findAll(AuthenticatedUser.requireId(), restaurantId)
        );
    }
}
