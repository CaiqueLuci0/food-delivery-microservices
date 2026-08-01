package food.delivery.user_ms.infra.adapters.outbound.persistence.mappers;

import food.delivery.user_ms.core.domain.entities.Adress;
import food.delivery.user_ms.core.domain.entities.User;
import food.delivery.user_ms.infra.adapters.outbound.persistence.jpaentities.JpaUser;

public final class UserPersistenceMapper {

    private UserPersistenceMapper() {
    }

    public static JpaUser toJpa(User user) {
        if (user == null) {
            return null;
        }

        JpaUser jpaUser = new JpaUser();
        jpaUser.setId(user.getId());
        jpaUser.setName(user.getName());
        jpaUser.setEmail(user.getEmail());
        jpaUser.setPassword(user.getPassword());
        jpaUser.setAdress(AdressPersistenceMapper.toJpa(user.getAdress()));

        return jpaUser;
    }

    public static User toDomain(JpaUser jpaUser) {
        if (jpaUser == null) {
            return null;
        }

        User user = new User();
        user.setId(jpaUser.getId());
        user.setName(jpaUser.getName());
        user.setEmail(jpaUser.getEmail());
        user.setPassword(jpaUser.getPassword());

        Adress adress = AdressPersistenceMapper.toDomain(jpaUser.getAdress());
        if (adress != null) {
            adress.setUser(user);
            user.setAdress(adress);
        }

        return user;
    }
}
