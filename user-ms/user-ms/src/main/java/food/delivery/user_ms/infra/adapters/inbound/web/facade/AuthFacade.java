package food.delivery.user_ms.infra.adapters.inbound.web.facade;

import food.delivery.user_ms.core.application.ports.in.AuthenticateUseCaseInputPort;
import food.delivery.user_ms.infra.adapters.inbound.web.presenter.dto.authcontroller.login.LoginMapper;
import food.delivery.user_ms.infra.adapters.inbound.web.presenter.dto.authcontroller.login.LoginRequestDto;
import food.delivery.user_ms.infra.adapters.inbound.web.presenter.dto.authcontroller.login.LoginResponseDto;
import org.springframework.stereotype.Component;

@Component
public class AuthFacade {

    private final AuthenticateUseCaseInputPort authenticateUseCase;

    public AuthFacade(AuthenticateUseCaseInputPort authenticateUseCase) {
        this.authenticateUseCase = authenticateUseCase;
    }

    public LoginResponseDto login(LoginRequestDto request) {
        return LoginMapper.toResponse(
                authenticateUseCase.login(request.getEmail(), request.getPassword())
        );
    }
}
