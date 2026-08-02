package food.delivery.restaurant_ms.core.domain.entities;

import java.util.UUID;

public class Restaurant {

    private UUID id;
    private String name;
    private String description;
    private UUID ownerId;
    private Address address;

    public Restaurant() {
    }

    public Restaurant(UUID id, String name, String description, UUID ownerId, Address address) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.ownerId = ownerId;
        this.address = address;
    }

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

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
