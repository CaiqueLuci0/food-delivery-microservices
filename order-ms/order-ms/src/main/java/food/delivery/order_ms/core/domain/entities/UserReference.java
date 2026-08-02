package food.delivery.order_ms.core.domain.entities;

import java.util.UUID;

public class UserReference {

    private UUID id;

    public UserReference() {
    }

    public UserReference(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
