package food.delivery.order_ms.infra.adapters.inbound.web.presenter.dto.ordercontroller.get;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ProductSnapshotResponseDto {

    private UUID id;
    private String name;
    private Double price;
    private String description;
    private List<SpecOptionSnapshotResponseDto> specOptionSnapshots = new ArrayList<>();

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

    public List<SpecOptionSnapshotResponseDto> getSpecOptionSnapshots() {
        return specOptionSnapshots;
    }

    public void setSpecOptionSnapshots(List<SpecOptionSnapshotResponseDto> specOptionSnapshots) {
        this.specOptionSnapshots = specOptionSnapshots != null ? specOptionSnapshots : new ArrayList<>();
    }
}
