package food.delivery.restaurant_ms.infra.adapters.outbound.persistence.mappers;

import food.delivery.restaurant_ms.core.domain.entities.UserReference;
import food.delivery.restaurant_ms.infra.adapters.outbound.persistence.jpaentities.JpaUserReference;

public final class UserReferencePersistenceMapper {

    private UserReferencePersistenceMapper() {
    }

    public static JpaUserReference toJpa(UserReference userReference) {
        if (userReference == null) {
            return null;
        }
        return new JpaUserReference(userReference.getId());
    }

    public static UserReference toDomain(JpaUserReference jpaUserReference) {
        if (jpaUserReference == null) {
            return null;
        }
        return new UserReference(jpaUserReference.getId());
    }
}
