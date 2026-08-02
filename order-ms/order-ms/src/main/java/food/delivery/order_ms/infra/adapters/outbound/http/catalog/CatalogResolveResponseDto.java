package food.delivery.order_ms.infra.adapters.outbound.http.catalog;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CatalogResolveResponseDto {

    private UUID restaurantId;
    private UUID ownerId;
    private List<ResolvedItemDto> items = new ArrayList<>();

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

    public List<ResolvedItemDto> getItems() {
        return items;
    }

    public void setItems(List<ResolvedItemDto> items) {
        this.items = items != null ? items : new ArrayList<>();
    }

    public static class ResolvedItemDto {

        private ProductDto product;
        private List<SpecOptionDto> specOptions = new ArrayList<>();

        public ProductDto getProduct() {
            return product;
        }

        public void setProduct(ProductDto product) {
            this.product = product;
        }

        public List<SpecOptionDto> getSpecOptions() {
            return specOptions;
        }

        public void setSpecOptions(List<SpecOptionDto> specOptions) {
            this.specOptions = specOptions != null ? specOptions : new ArrayList<>();
        }
    }

    public static class ProductDto {

        private String name;
        private Double price;
        private String description;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Double getPrice() {
            return price;
        }

        public void setPrice(Double price) {
            this.price = price;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }

    public static class SpecOptionDto {

        private String name;
        private String description;
        private Double extraPrice;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Double getExtraPrice() {
            return extraPrice;
        }

        public void setExtraPrice(Double extraPrice) {
            this.extraPrice = extraPrice;
        }
    }
}
