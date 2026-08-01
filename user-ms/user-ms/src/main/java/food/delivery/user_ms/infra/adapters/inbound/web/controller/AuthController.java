package food.delivery.user_ms.infra.adapters.inbound.web.controller;

import food.delivery.user_ms.infra.adapters.inbound.web.presenter.dto.authcontroller.login.LoginRequestDto;
import food.delivery.user_ms.infra.adapters.inbound.web.presenter.dto.authcontroller.login.LoginResponseDto;
import food.delivery.user_ms.infra.adapters.inbound.web.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
