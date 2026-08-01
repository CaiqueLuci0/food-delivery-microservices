package food.delivery.user_ms.core.application.usecases;

import food.delivery.user_ms.core.application.ports.in.AuthenticateUseCaseInputPort;
import food.delivery.user_ms.core.application.ports.out.PasswordEncoderOutputPort;
import food.delivery.user_ms.core.application.ports.out.TokenOutputPort;
import food.delivery.user_ms.core.application.ports.out.UserRepositoryOutputPort;
import food.delivery.user_ms.core.domain.entities.User;
import food.delivery.user_ms.core.domain.entities.UserLoginDetails;
import food.delivery.user_ms.core.domain.enums.ConstMessagesEnum;
import food.delivery.user_ms.core.domain.exceptions.UnauthorizedException;

public class AuthenticateUseCase implements AuthenticateUseCaseInputPort {

    private final UserRepositoryOutputPort userRepositoryOutputPort;
    private final PasswordEncoderOutputPort passwordEncoderOutputPort;
    private final TokenOutputPort tokenOutputPort;

    public AuthenticateUseCase(
            UserRepositoryOutputPort userRepositoryOutputPort,
            PasswordEncoderOutputPort passwordEncoderOutputPort,
            TokenOutputPort tokenOutputPort
    ) {
        this.userRepositoryOutputPort = userRepositoryOutputPort;
        this.passwordEncoderOutputPort = passwordEncoderOutputPort;
        this.tokenOutputPort = tokenOutputPort;
    }

    @Override
    public UserLoginDetails login(String email, String password) {
        User user = userRepositoryOutputPort.findByEmail(email)
                .orElseThrow(() -> new UnauthorizedException(ConstMessagesEnum.INVALID_CREDENTIALS.getMessage()));

        if (!passwordEncoderOutputPort.matches(password, user.getPassword())) {
            throw new UnauthorizedException(ConstMessagesEnum.INVALID_CREDENTIALS.getMessage());
        }

        return new UserLoginDetails(tokenOutputPort.generate(user), user);
    }
}
