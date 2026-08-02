package food.delivery.user_ms.infra.adapters.inbound.web.controller;

import food.delivery.user_ms.core.application.ports.in.UserCrudUseCaseInputPort;
import food.delivery.user_ms.core.domain.entities.User;
import food.delivery.user_ms.infra.adapters.inbound.web.presenter.dto.usercontroller.create.UserCreateMapper;
import food.delivery.user_ms.infra.adapters.inbound.web.presenter.dto.usercontroller.create.UserCreateRequestDto;
import food.delivery.user_ms.infra.adapters.inbound.web.presenter.dto.usercontroller.get.UserResponseDto;
import food.delivery.user_ms.infra.adapters.inbound.web.presenter.dto.usercontroller.get.UserResponseMapper;
import food.delivery.user_ms.infra.adapters.inbound.web.presenter.dto.usercontroller.update.UserUpdateMapper;
import food.delivery.user_ms.infra.adapters.inbound.web.presenter.dto.usercontroller.update.UserUpdateRequestDto;
import food.delivery.user_ms.infra.adapters.inbound.web.security.AuthenticatedUser;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserCrudUseCaseInputPort userCrudUseCase;

    public UserController(UserCrudUseCaseInputPort userCrudUseCase) {
        this.userCrudUseCase = userCrudUseCase;
    }

    @PostMapping
    public ResponseEntity<UserResponseDto> create(@Valid @RequestBody UserCreateRequestDto request) {
        User created = userCrudUseCase.create(
                UserCreateMapper.toUser(request),
                UserCreateMapper.toAddress(request.getAddress())
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(UserResponseMapper.toResponse(created));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(UserResponseMapper.toResponse(userCrudUseCase.findById(id)));
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> findAll() {
        return ResponseEntity.ok(UserResponseMapper.toResponseList(userCrudUseCase.findAll()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> update(
            @PathVariable UUID id,
            @Valid @RequestBody UserUpdateRequestDto request
    ) {
        User updated = userCrudUseCase.update(
                AuthenticatedUser.requireId(),
                id,
                UserUpdateMapper.toUser(request)
        );
        return ResponseEntity.ok(UserResponseMapper.toResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        userCrudUseCase.delete(AuthenticatedUser.requireId(), id);
        return ResponseEntity.noContent().build();
    }
}
