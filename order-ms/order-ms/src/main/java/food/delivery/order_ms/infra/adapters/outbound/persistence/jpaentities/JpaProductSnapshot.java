package food.delivery.order_ms.infra.adapters.outbound.persistence.jpaentities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "product_snapshot")
public class JpaProductSnapshot {

    @Id
    private UUID id;

    private String name;

    private Double price;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private JpaOrder order;

    @OneToMany(mappedBy = "productSnapshot", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Fetch(FetchMode.SUBSELECT)
    private List<JpaSpecOptionSnapshot> specOptionSnapshots = new ArrayList<>();

    @PrePersist
    void prePersist() {
        if (id == null) {
            id = UUID.randomUUID();
        }
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public JpaOrder getOrder() {
        return order;
    }

    public void setOrder(JpaOrder order) {
        this.order = order;
    }

    public List<JpaSpecOptionSnapshot> getSpecOptionSnapshots() {
        return specOptionSnapshots;
    }

    public void setSpecOptionSnapshots(List<JpaSpecOptionSnapshot> specOptionSnapshots) {
        this.specOptionSnapshots = specOptionSnapshots != null ? specOptionSnapshots : new ArrayList<>();
    }
}
