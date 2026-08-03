package food.delivery.order_ms.infra.adapters.outbound.websocket;

import food.delivery.order_ms.core.application.ports.out.OrderStatusPushOutputPort;
import food.delivery.order_ms.core.domain.entities.Order;
import food.delivery.order_ms.infra.adapters.inbound.web.presenter.dto.ordercontroller.get.OrderResponseMapper;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderStatusPushOutputPortAdapter implements OrderStatusPushOutputPort {

    private final SimpMessagingTemplate messagingTemplate;

    public OrderStatusPushOutputPortAdapter(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public void push(Order order) {
        if (order == null || order.getId() == null) {
            return;
        }
        messagingTemplate.convertAndSend(
                "/topic/orders/" + order.getId(),
                OrderResponseMapper.toResponse(order)
        );
    }
}
