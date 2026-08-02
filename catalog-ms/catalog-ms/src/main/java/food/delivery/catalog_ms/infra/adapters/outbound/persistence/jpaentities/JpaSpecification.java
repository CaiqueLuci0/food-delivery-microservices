package food.delivery.catalog_ms.infra.adapters.outbound.persistence.jpaentities;

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
@Table(name = "specification")
public class JpaSpecification {

    @Id
    private UUID id;

    private String name;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private JpaProduct product;

    @OneToMany(mappedBy = "specification", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Fetch(FetchMode.SUBSELECT)
    private List<JpaSpecOption> specOptions = new ArrayList<>();

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public JpaProduct getProduct() {
        return product;
    }

    public void setProduct(JpaProduct product) {
        this.product = product;
    }

    public List<JpaSpecOption> getSpecOptions() {
        return specOptions;
    }

    public void setSpecOptions(List<JpaSpecOption> specOptions) {
        this.specOptions = specOptions != null ? specOptions : new ArrayList<>();
    }

    public UUID getProductId() {
        return product != null ? product.getId() : null;
    }
}
