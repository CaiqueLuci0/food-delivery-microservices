package food.delivery.order_ms.infra.adapters.outbound.http.catalog;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CatalogResolveRequestDto {

    private List<ResolveItemDto> items = new ArrayList<>();

    public List<ResolveItemDto> getItems() {
        return items;
    }

    public void setItems(List<ResolveItemDto> items) {
        this.items = items != null ? items : new ArrayList<>();
    }

    public static class ResolveItemDto {

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
