package food.delivery.catalog_ms.infra.adapters.inbound.web.presenter.dto.productcontroller.update;

import com.fasterxml.jackson.annotation.JsonSetter;
import food.delivery.catalog_ms.infra.adapters.inbound.web.presenter.dto.productcontroller.create.SpecificationRequestDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ProductUpdateRequestDto {

    @NotBlank
    private String name;

    @NotNull
    private Double price;

    private String description;

    @Valid
    private List<SpecificationRequestDto> specifications;

    private boolean specificationsPresent;

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

    public List<SpecificationRequestDto> getSpecifications() {
        return specifications;
    }

    @JsonSetter("specifications")
    public void setSpecifications(List<SpecificationRequestDto> specifications) {
        this.specifications = specifications != null ? specifications : new ArrayList<>();
        this.specificationsPresent = true;
    }

    public boolean isSpecificationsPresent() {
        return specificationsPresent;
    }
}
