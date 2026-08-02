package food.delivery.order_ms.core.domain.entities;

import java.util.UUID;

public class SpecOptionSnapshot {

    private UUID id;
    private String name;
    private String description;
    private Double extraPrice;
    private ProductSnapshot productSnapshot;

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

    public ProductSnapshot getProductSnapshot() {
        return productSnapshot;
    }

    public void setProductSnapshot(ProductSnapshot productSnapshot) {
        this.productSnapshot = productSnapshot;
    }
}
