package food.delivery.restaurant_ms.infra.adapters.inbound.web.presenter.dto.restaurantcontroller.create;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class RestaurantCreateRequestDto {

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @NotNull
    @Valid
    private AddressRequestDto address;

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

    public AddressRequestDto getAddress() {
        return address;
    }

    public void setAddress(AddressRequestDto address) {
        this.address = address;
    }
}
