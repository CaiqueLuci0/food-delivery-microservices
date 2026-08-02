package food.delivery.restaurant_ms.infra.adapters.inbound.web.controller;

import food.delivery.restaurant_ms.infra.adapters.inbound.web.presenter.dto.restaurantcontroller.create.RestaurantCreateRequestDto;
import food.delivery.restaurant_ms.infra.adapters.inbound.web.presenter.dto.restaurantcontroller.get.RestaurantResponseDto;
import food.delivery.restaurant_ms.infra.adapters.inbound.web.presenter.dto.restaurantcontroller.update.RestaurantUpdateRequestDto;
import food.delivery.restaurant_ms.infra.adapters.inbound.web.service.RestaurantService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/restaurants")
public class RestaurantController {

    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @PostMapping
    public ResponseEntity<RestaurantResponseDto> create(@Valid @RequestBody RestaurantCreateRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(restaurantService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantResponseDto> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(restaurantService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<RestaurantResponseDto>> findAll(@RequestParam(required = false) String search) {
        return ResponseEntity.ok(restaurantService.findAll(search));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RestaurantResponseDto> update(
            @PathVariable UUID id,
            @Valid @RequestBody RestaurantUpdateRequestDto request
    ) {
        return ResponseEntity.ok(restaurantService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        restaurantService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
