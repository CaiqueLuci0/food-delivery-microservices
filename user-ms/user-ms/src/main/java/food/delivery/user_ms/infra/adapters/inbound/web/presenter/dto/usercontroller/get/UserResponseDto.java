package food.delivery.user_ms.infra.adapters.inbound.web.presenter.dto.usercontroller.get;

import food.delivery.user_ms.infra.adapters.inbound.web.presenter.dto.usercontroller.create.AdressResponseDto;

import java.util.UUID;

public class UserResponseDto {

    private UUID id;
    private String name;
    private String email;
    private AdressResponseDto adress;

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

    public AdressResponseDto getAdress() {
        return adress;
    }

    public void setAdress(AdressResponseDto adress) {
        this.adress = adress;
    }
}
