package food.delivery.user_ms.infra.adapters.outbound.persistence.jpaentities;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "users")
public class JpaUser {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String name;
    private String email;
    private String password;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private JpaAddress address;

    public JpaUser() {
    }

    public JpaUser(UUID id, String name, String email, String password, JpaAddress address) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public JpaAddress getAddress() {
        return address;
    }

    public void setAddress(JpaAddress address) {
        this.address = address;
        if (address != null) {
            address.setUser(this);
        }
    }
}
