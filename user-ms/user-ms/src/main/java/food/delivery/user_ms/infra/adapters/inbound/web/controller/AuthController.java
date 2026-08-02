package food.delivery.user_ms.infra.adapters.inbound.web.controller;

import food.delivery.user_ms.core.application.ports.in.AuthenticateUseCaseInputPort;
import food.delivery.user_ms.infra.adapters.inbound.web.presenter.dto.authcontroller.login.LoginMapper;
import food.delivery.user_ms.infra.adapters.inbound.web.presenter.dto.authcontroller.login.LoginRequestDto;
import food.delivery.user_ms.infra.adapters.inbound.web.presenter.dto.authcontroller.login.LoginResponseDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticateUseCaseInputPort authenticateUseCase;

    public AuthController(AuthenticateUseCaseInputPort authenticateUseCase) {
        this.authenticateUseCase = authenticateUseCase;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto request) {
        return ResponseEntity.ok(LoginMapper.toResponse(
                authenticateUseCase.login(request.getEmail(), request.getPassword())
        ));
    }
}
