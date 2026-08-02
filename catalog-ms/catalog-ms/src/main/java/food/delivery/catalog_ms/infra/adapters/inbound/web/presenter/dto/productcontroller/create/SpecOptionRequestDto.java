package food.delivery.catalog_ms.infra.adapters.inbound.web.presenter.dto.productcontroller.create;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class SpecOptionRequestDto {

    @NotBlank
    private String name;

    private String description;

    @NotNull
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
