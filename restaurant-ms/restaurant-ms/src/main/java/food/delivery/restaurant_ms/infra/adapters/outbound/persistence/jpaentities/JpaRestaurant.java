package food.delivery.restaurant_ms.infra.adapters.outbound.persistence.jpaentities;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Entity
@Table(name = "restaurant")
public class JpaRestaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(SqlTypes.CHAR)
    private UUID id;
    private String name;
    private String description;

    @Column(name = "owner_id", nullable = false, unique = true)
    @JdbcTypeCode(SqlTypes.CHAR)
    private UUID ownerId;

    @Column(name = "image_key", length = 512)
    private String imageKey;

    @OneToOne(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    private JpaAddress address;

    public JpaRestaurant() {
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

    public String getImageKey() {
        return imageKey;
    }

    public void setImageKey(String imageKey) {
        this.imageKey = imageKey;
    }

    public JpaAddress getAddress() {
        return address;
    }

    public void setAddress(JpaAddress address) {
        this.address = address;
        if (address != null) {
            address.setRestaurant(this);
        }
    }
}
