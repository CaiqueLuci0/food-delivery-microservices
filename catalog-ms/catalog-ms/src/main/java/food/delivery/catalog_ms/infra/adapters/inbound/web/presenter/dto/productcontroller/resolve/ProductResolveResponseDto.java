package food.delivery.catalog_ms.infra.adapters.inbound.web.presenter.dto.productcontroller.resolve;

import food.delivery.catalog_ms.infra.adapters.inbound.web.presenter.dto.productcontroller.get.ProductResponseDto;
import food.delivery.catalog_ms.infra.adapters.inbound.web.presenter.dto.productcontroller.get.SpecOptionResponseDto;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ProductResolveResponseDto {

    private UUID restaurantId;
    private UUID ownerId;
    private List<ResolvedItemResponseDto> items = new ArrayList<>();

    public UUID getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(UUID restaurantId) {
        this.restaurantId = restaurantId;
    }

    public UUID getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(UUID ownerId) {
        this.ownerId = ownerId;
    }

    public List<ResolvedItemResponseDto> getItems() {
        return items;
    }

    public void setItems(List<ResolvedItemResponseDto> items) {
        this.items = items != null ? items : new ArrayList<>();
    }

    public static class ResolvedItemResponseDto {

        private ProductResponseDto product;
        private List<SpecOptionResponseDto> specOptions = new ArrayList<>();

        public ProductResponseDto getProduct() {
            return product;
        }

        public void setProduct(ProductResponseDto product) {
            this.product = product;
        }

        public List<SpecOptionResponseDto> getSpecOptions() {
            return specOptions;
        }

        public void setSpecOptions(List<SpecOptionResponseDto> specOptions) {
            this.specOptions = specOptions != null ? specOptions : new ArrayList<>();
        }
    }
}
