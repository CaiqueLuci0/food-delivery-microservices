package food.delivery.catalog_ms.infra.adapters.inbound.web.presenter.dto.productcontroller.get;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SpecificationResponseDto {

    private UUID id;
    private String name;
    private String description;
    private List<SpecOptionResponseDto> specOptions = new ArrayList<>();

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

    public List<SpecOptionResponseDto> getSpecOptions() {
        return specOptions;
    }

    public void setSpecOptions(List<SpecOptionResponseDto> specOptions) {
        this.specOptions = specOptions != null ? specOptions : new ArrayList<>();
    }
}
