package food.delivery.user_ms.infra.adapters.inbound.web.presenter.dto.usercontroller.get;

import food.delivery.user_ms.infra.adapters.inbound.web.presenter.dto.usercontroller.create.AddressResponseDto;

import java.util.UUID;

public class UserResponseDto {

    private UUID id;
    private String name;
    private String email;
    private AddressResponseDto address;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public AddressResponseDto getAddress() {
        return address;
    }

    public void setAddress(AddressResponseDto address) {
        this.address = address;
    }
}
