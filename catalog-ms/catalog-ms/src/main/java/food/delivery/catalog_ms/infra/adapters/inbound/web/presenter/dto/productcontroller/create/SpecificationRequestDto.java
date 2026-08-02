package food.delivery.catalog_ms.infra.adapters.inbound.web.presenter.dto.productcontroller.create;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;

public class SpecificationRequestDto {

    @NotBlank
    private String name;

    private String description;

    @Valid
    private List<SpecOptionRequestDto> specOptions = new ArrayList<>();

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

    public List<SpecOptionRequestDto> getSpecOptions() {
        return specOptions;
    }

    public void setSpecOptions(List<SpecOptionRequestDto> specOptions) {
        this.specOptions = specOptions != null ? specOptions : new ArrayList<>();
    }
}
