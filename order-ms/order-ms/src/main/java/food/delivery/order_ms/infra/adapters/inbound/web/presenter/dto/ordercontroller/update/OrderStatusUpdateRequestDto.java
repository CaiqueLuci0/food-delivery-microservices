package food.delivery.order_ms.infra.adapters.inbound.web.presenter.dto.ordercontroller.update;

import food.delivery.order_ms.core.domain.enums.OrderStatus;
import jakarta.validation.constraints.NotNull;

public class OrderStatusUpdateRequestDto {

    @NotNull
    private OrderStatus status;

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}
