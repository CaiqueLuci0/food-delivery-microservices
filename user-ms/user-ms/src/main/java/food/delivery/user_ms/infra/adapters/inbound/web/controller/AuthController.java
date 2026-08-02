package food.delivery.user_ms.infra.adapters.inbound.web.controller;

import food.delivery.user_ms.infra.adapters.inbound.web.facade.AuthFacade;
import food.delivery.user_ms.infra.adapters.inbound.web.presenter.dto.authcontroller.login.LoginRequestDto;
import food.delivery.user_ms.infra.adapters.inbound.web.presenter.dto.authcontroller.login.LoginResponseDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthFacade authFacade;

    public AuthController(AuthFacade authFacade) {
        this.authFacade = authFacade;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto request) {
        return ResponseEntity.ok(authFacade.login(request));
    }

    @GetMapping("/islogged")
    public ResponseEntity<LoginResponseDto> isLogged(@RequestParam String token) {
        return ResponseEntity.ok(authFacade.isLogged(token));
    }
}
