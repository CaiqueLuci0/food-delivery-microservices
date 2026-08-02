package food.delivery.order_ms.infra.adapters.inbound.web.presenter.dto.ordercontroller.update;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OrderUpdateRequestDto {

    @NotEmpty
    @Valid
    private List<OrderItemRequestDto> items = new ArrayList<>();

    public List<OrderItemRequestDto> getItems() {
        return items;
    }

    public void setItems(List<OrderItemRequestDto> items) {
        this.items = items != null ? items : new ArrayList<>();
    }

    public static class OrderItemRequestDto {

        @NotNull
        private UUID productId;

        private List<UUID> specOptionIds = new ArrayList<>();

        public UUID getProductId() {
            return productId;
        }

        public void setProductId(UUID productId) {
            this.productId = productId;
        }

        public List<UUID> getSpecOptionIds() {
            return specOptionIds;
        }

        public void setSpecOptionIds(List<UUID> specOptionIds) {
            this.specOptionIds = specOptionIds != null ? specOptionIds : new ArrayList<>();
        }
    }
}
