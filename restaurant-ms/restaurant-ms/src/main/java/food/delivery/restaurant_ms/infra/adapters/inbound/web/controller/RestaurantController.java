package food.delivery.restaurant_ms.infra.adapters.inbound.web.controller;

import food.delivery.restaurant_ms.core.application.ports.in.RestaurantCrudUseCaseInputPort;
import food.delivery.restaurant_ms.core.domain.entities.Restaurant;
import food.delivery.restaurant_ms.infra.adapters.inbound.web.presenter.dto.restaurantcontroller.create.RestaurantCreateMapper;
import food.delivery.restaurant_ms.infra.adapters.inbound.web.presenter.dto.restaurantcontroller.create.RestaurantCreateRequestDto;
import food.delivery.restaurant_ms.infra.adapters.inbound.web.presenter.dto.restaurantcontroller.get.RestaurantResponseDto;
import food.delivery.restaurant_ms.infra.adapters.inbound.web.presenter.dto.restaurantcontroller.get.RestaurantResponseMapper;
import food.delivery.restaurant_ms.infra.adapters.inbound.web.presenter.dto.restaurantcontroller.update.RestaurantUpdateMapper;
import food.delivery.restaurant_ms.infra.adapters.inbound.web.presenter.dto.restaurantcontroller.update.RestaurantUpdateRequestDto;
import food.delivery.restaurant_ms.infra.adapters.inbound.web.security.AuthenticatedUser;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/restaurants")
public class RestaurantController {

    private final RestaurantCrudUseCaseInputPort restaurantCrudUseCase;

    public RestaurantController(RestaurantCrudUseCaseInputPort restaurantCrudUseCase) {
        this.restaurantCrudUseCase = restaurantCrudUseCase;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<RestaurantResponseDto> create(@Valid @RequestBody RestaurantCreateRequestDto request) {
        Restaurant created = restaurantCrudUseCase.create(
                AuthenticatedUser.requireId(),
                RestaurantCreateMapper.toRestaurant(request),
                RestaurantCreateMapper.toAddress(request.getAddress())
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(RestaurantResponseMapper.toResponse(created));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantResponseDto> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(RestaurantResponseMapper.toResponse(restaurantCrudUseCase.findById(id)));
    }

    @GetMapping
    public ResponseEntity<List<RestaurantResponseDto>> findAll(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) BigDecimal latitude,
            @RequestParam(required = false) BigDecimal longitude
    ) {
        return ResponseEntity.ok(RestaurantResponseMapper.toResponseList(
                restaurantCrudUseCase.findAll(search, latitude, longitude)
        ));
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<RestaurantResponseDto> update(
            @PathVariable UUID id,
            @Valid @RequestBody RestaurantUpdateRequestDto request
    ) {
        Restaurant updated = restaurantCrudUseCase.update(
                AuthenticatedUser.requireId(),
                id,
                RestaurantUpdateMapper.toRestaurant(request),
                RestaurantUpdateMapper.toAddress(request)
        );
        return ResponseEntity.ok(RestaurantResponseMapper.toResponse(updated));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        restaurantCrudUseCase.delete(AuthenticatedUser.requireId(), id);
        return ResponseEntity.noContent().build();
    }
}
