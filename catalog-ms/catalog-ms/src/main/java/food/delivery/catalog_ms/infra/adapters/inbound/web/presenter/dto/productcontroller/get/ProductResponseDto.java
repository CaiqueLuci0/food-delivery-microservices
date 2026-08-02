package food.delivery.catalog_ms.infra.adapters.inbound.web.presenter.dto.productcontroller.get;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ProductResponseDto {

    private UUID id;
    private String name;
    private Double price;
    private String description;
    private UUID ownerId;
    private UUID restaurantId;
    private List<SpecificationResponseDto> specifications = new ArrayList<>();

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

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

    public UUID getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(UUID ownerId) {
        this.ownerId = ownerId;
    }

    public UUID getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(UUID restaurantId) {
        this.restaurantId = restaurantId;
    }

    public List<SpecificationResponseDto> getSpecifications() {
        return specifications;
    }

    public void setSpecifications(List<SpecificationResponseDto> specifications) {
        this.specifications = specifications != null ? specifications : new ArrayList<>();
    }
}
