package food.delivery.order_ms.infra.adapters.outbound.persistence.jpaentities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "user_reference")
public class JpaUserReference {

    @Id
    private UUID id;

    public JpaUserReference() {
    }

    public JpaUserReference(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
