package food.delivery.order_ms.core.domain.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ProductSnapshot {

    private UUID id;
    private String name;
    private Double price;
    private String description;
    private Order order;
    private List<SpecOptionSnapshot> specOptionSnapshots = new ArrayList<>();

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

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public List<SpecOptionSnapshot> getSpecOptionSnapshots() {
        return specOptionSnapshots;
    }

    public void setSpecOptionSnapshots(List<SpecOptionSnapshot> specOptionSnapshots) {
        this.specOptionSnapshots = specOptionSnapshots != null ? specOptionSnapshots : new ArrayList<>();
    }
}
