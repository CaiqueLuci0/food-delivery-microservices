package food.delivery.user_ms.core.application.ports.in;

import food.delivery.user_ms.core.domain.entities.Adress;
import food.delivery.user_ms.core.domain.entities.User;

import java.util.List;
import java.util.UUID;

public interface UserCrudUseCaseInputPort {
    User findById(UUID id);
    List<User> findAll();
    User create(User user, Adress adress);
    User update(UUID userid, User user);
    void delete(UUID userid);
}
