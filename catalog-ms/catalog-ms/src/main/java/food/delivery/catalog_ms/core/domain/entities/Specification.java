package food.delivery.catalog_ms.core.domain.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Specification {

    private UUID id;
    private String name;
    private String description;
    private Product product;
    private List<SpecOption> specOptions = new ArrayList<>();

    public Specification() {
    }

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

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public List<SpecOption> getSpecOptions() {
        return specOptions;
    }

    public void setSpecOptions(List<SpecOption> specOptions) {
        this.specOptions = specOptions != null ? specOptions : new ArrayList<>();
    }
}
