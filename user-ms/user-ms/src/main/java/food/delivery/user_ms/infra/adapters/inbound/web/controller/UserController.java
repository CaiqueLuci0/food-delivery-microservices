package food.delivery.user_ms.infra.adapters.inbound.web.controller;

import food.delivery.user_ms.infra.adapters.inbound.web.facade.UserFacade;
import food.delivery.user_ms.infra.adapters.inbound.web.presenter.dto.usercontroller.create.UserCreateRequestDto;
import food.delivery.user_ms.infra.adapters.inbound.web.presenter.dto.usercontroller.get.UserResponseDto;
import food.delivery.user_ms.infra.adapters.inbound.web.presenter.dto.usercontroller.update.UserUpdateRequestDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserFacade userFacade;

    public UserController(UserFacade userFacade) {
        this.userFacade = userFacade;
    }

    @PostMapping
    public ResponseEntity<UserResponseDto> create(@Valid @RequestBody UserCreateRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userFacade.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(userFacade.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> findAll() {
        return ResponseEntity.ok(userFacade.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> update(
            @PathVariable UUID id,
            @Valid @RequestBody UserUpdateRequestDto request
    ) {
        return ResponseEntity.ok(userFacade.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        userFacade.delete(id);
        return ResponseEntity.noContent().build();
    }
}
