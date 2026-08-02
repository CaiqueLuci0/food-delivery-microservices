package food.delivery.order_ms.infra.adapters.inbound.web.presenter.dto.ordercontroller.get;

import java.util.UUID;

public class SpecOptionSnapshotResponseDto {

    private UUID id;
    private String name;
    private String description;
    private Double extraPrice;

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
}
