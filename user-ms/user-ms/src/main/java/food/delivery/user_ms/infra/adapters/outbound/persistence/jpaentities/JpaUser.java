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
    private JpaAdress adress;

    public JpaUser() {
    }

    public JpaUser(UUID id, String name, String email, String password, JpaAdress adress) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.adress = adress;
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

    public JpaAdress getAdress() {
        return adress;
    }

    public void setAdress(JpaAdress adress) {
        this.adress = adress;
        if (adress != null) {
            adress.setUser(this);
        }
    }
}
