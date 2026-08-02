package food.delivery.restaurant_ms.infra.adapters.inbound.web.presenter.dto.restaurantcontroller.get;

import java.util.UUID;

public class RestaurantResponseDto {

    private UUID id;
    private String name;
    private String description;
    private UUID ownerId;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UUID getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(UUID ownerId) {
        this.ownerId = ownerId;
    }

    public AddressResponseDto getAddress() {
        return address;
    }

    public void setAddress(AddressResponseDto address) {
        this.address = address;
    }
}
