package food.delivery.restaurant_ms.infra.adapters.inbound.web.controller;

import food.delivery.restaurant_ms.infra.adapters.inbound.web.facade.RestaurantFacade;
import food.delivery.restaurant_ms.infra.adapters.inbound.web.presenter.dto.restaurantcontroller.create.RestaurantCreateRequestDto;
import food.delivery.restaurant_ms.infra.adapters.inbound.web.presenter.dto.restaurantcontroller.get.RestaurantResponseDto;
import food.delivery.restaurant_ms.infra.adapters.inbound.web.presenter.dto.restaurantcontroller.update.RestaurantUpdateRequestDto;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/restaurants")
public class RestaurantController {

    private final RestaurantFacade restaurantFacade;

    public RestaurantController(RestaurantFacade restaurantFacade) {
        this.restaurantFacade = restaurantFacade;
    }

    @PostMapping
    public ResponseEntity<RestaurantResponseDto> create(@Valid @RequestBody RestaurantCreateRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(restaurantFacade.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantResponseDto> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(restaurantFacade.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<RestaurantResponseDto>> findAll(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) BigDecimal latitude,
            @RequestParam(required = false) BigDecimal longitude
    ) {
        return ResponseEntity.ok(restaurantFacade.findAll(search, latitude, longitude));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RestaurantResponseDto> update(
            @PathVariable UUID id,
            @Valid @RequestBody RestaurantUpdateRequestDto request
    ) {
        return ResponseEntity.ok(restaurantFacade.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        restaurantFacade.delete(id);
        return ResponseEntity.noContent().build();
    }
}
