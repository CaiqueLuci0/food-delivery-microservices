package food.delivery.catalog_ms.infra.adapters.inbound.web.presenter.dto.productcontroller.resolve;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ProductResolveRequestDto {

    @NotEmpty
    @Valid
    private List<ResolveItemRequestDto> items = new ArrayList<>();

    public List<ResolveItemRequestDto> getItems() {
        return items;
    }

    public void setItems(List<ResolveItemRequestDto> items) {
        this.items = items != null ? items : new ArrayList<>();
    }

    public static class ResolveItemRequestDto {

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
