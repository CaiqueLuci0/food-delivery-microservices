package food.delivery.user_ms.infra.adapters.inbound.web.presenter.dto.usercontroller.update;

import food.delivery.user_ms.infra.adapters.inbound.web.presenter.dto.usercontroller.create.AddressRequestDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class UserUpdateRequestDto {

    @NotBlank
    private String name;

    @NotNull
    @Valid
    private AddressRequestDto address;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AddressRequestDto getAddress() {
        return address;
    }

    public void setAddress(AddressRequestDto address) {
        this.address = address;
    }
}
