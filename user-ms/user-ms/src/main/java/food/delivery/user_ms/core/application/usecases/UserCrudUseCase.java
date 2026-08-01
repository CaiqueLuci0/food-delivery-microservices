package food.delivery.user_ms.core.application.usecases;

import food.delivery.user_ms.core.application.ports.in.UserCrudUseCaseInputPort;
import food.delivery.user_ms.core.application.ports.out.PasswordEncoderOutputPort;
import food.delivery.user_ms.core.application.ports.out.UserCreatedEventOutputPort;
import food.delivery.user_ms.core.application.ports.out.UserDeletedEventOutputPort;
import food.delivery.user_ms.core.application.ports.out.UserRepositoryOutputPort;
import food.delivery.user_ms.core.domain.entities.Adress;
import food.delivery.user_ms.core.domain.entities.User;
import food.delivery.user_ms.core.domain.enums.ConstMessagesEnum;
import food.delivery.user_ms.core.domain.exceptions.ConflictException;
import food.delivery.user_ms.core.domain.exceptions.ForbiddenException;
import food.delivery.user_ms.core.domain.exceptions.NotFoundException;

import java.util.List;
import java.util.UUID;

public class UserCrudUseCase implements UserCrudUseCaseInputPort {

    private final UserRepositoryOutputPort userRepositoryOutputPort;
    private final PasswordEncoderOutputPort passwordEncoderOutputPort;
    private final UserCreatedEventOutputPort userCreatedEventOutputPort;
    private final UserDeletedEventOutputPort userDeletedEventOutputPort;

    public UserCrudUseCase(
            UserRepositoryOutputPort userRepositoryOutputPort,
            PasswordEncoderOutputPort passwordEncoderOutputPort,
            UserCreatedEventOutputPort userCreatedEventOutputPort,
            UserDeletedEventOutputPort userDeletedEventOutputPort
    ) {
        this.userRepositoryOutputPort = userRepositoryOutputPort;
        this.passwordEncoderOutputPort = passwordEncoderOutputPort;
        this.userCreatedEventOutputPort = userCreatedEventOutputPort;
        this.userDeletedEventOutputPort = userDeletedEventOutputPort;
    }

    @Override
    public User findById(UUID id) {
        return this.userRepositoryOutputPort.findById(id)
                .orElseThrow(() -> new NotFoundException(ConstMessagesEnum.NOT_FOUND.getMessage()));
    }

    @Override
    public List<User> findAll() {
        return this.userRepositoryOutputPort.findAll();
    }

    @Override
    public User create(User user, Adress adress) {
        if (adress == null) {
            throw new ConflictException(ConstMessagesEnum.INVALID_REQUEST.getMessage());
        }
        if (this.userRepositoryOutputPort.existsByEmail(user.getEmail())) {
            throw new ConflictException(ConstMessagesEnum.EMAIL_ALREADY_EXISTS.getMessage());
        }
        user.setPassword(passwordEncoderOutputPort.encode(user.getPassword()));
        user.setAdress(adress);
        adress.setUser(user);
        User saved = this.userRepositoryOutputPort.save(user);
        userCreatedEventOutputPort.publish(saved.getId());
        return saved;
    }

    @Override
    public User update(UUID authenticatedUserId, UUID userid, User user) {
        User existingUser = this.findById(userid);
        assertSameUser(authenticatedUserId, existingUser);
        existingUser.setName(user.getName());
        return this.userRepositoryOutputPort.save(existingUser);
    }

    @Override
    public void delete(UUID authenticatedUserId, UUID userid) {
        User existingUser = this.findById(userid);
        assertSameUser(authenticatedUserId, existingUser);
        UUID deletedUserId = existingUser.getId();
        this.userRepositoryOutputPort.delete(existingUser);
        userDeletedEventOutputPort.publish(deletedUserId);
    }

    private void assertSameUser(UUID authenticatedUserId, User existingUser) {
        if (!authenticatedUserId.equals(existingUser.getId())) {
            throw new ForbiddenException(ConstMessagesEnum.ACCESS_DENIED.getMessage());
        }
    }
}
