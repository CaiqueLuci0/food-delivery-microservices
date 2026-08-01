package food.delivery.user_ms.infra.adapters.inbound.web.service;

import food.delivery.user_ms.core.application.ports.in.AuthenticateUseCaseInputPort;
import food.delivery.user_ms.core.domain.entities.UserLoginDetails;
import food.delivery.user_ms.infra.adapters.inbound.web.presenter.dto.authcontroller.login.LoginMapper;
import food.delivery.user_ms.infra.adapters.inbound.web.presenter.dto.authcontroller.login.LoginRequestDto;
import food.delivery.user_ms.infra.adapters.inbound.web.presenter.dto.authcontroller.login.LoginResponseDto;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticateUseCaseInputPort authenticateUseCase;

    public AuthService(AuthenticateUseCaseInputPort authenticateUseCase) {
        this.authenticateUseCase = authenticateUseCase;
    }

    public LoginResponseDto login(LoginRequestDto request) {
        UserLoginDetails userLoginDetails = authenticateUseCase.login(request.getEmail(), request.getPassword());
        return LoginMapper.toResponse(userLoginDetails);
    }
}
