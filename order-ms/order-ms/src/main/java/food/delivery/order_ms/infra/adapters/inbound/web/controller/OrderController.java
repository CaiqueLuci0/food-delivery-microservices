package food.delivery.order_ms.infra.adapters.inbound.web.controller;

import food.delivery.order_ms.infra.adapters.inbound.web.facade.OrderFacade;
import food.delivery.order_ms.infra.adapters.inbound.web.presenter.dto.ordercontroller.create.OrderCreateRequestDto;
import food.delivery.order_ms.infra.adapters.inbound.web.presenter.dto.ordercontroller.get.OrderResponseDto;
import food.delivery.order_ms.infra.adapters.inbound.web.presenter.dto.ordercontroller.update.OrderStatusUpdateRequestDto;
import food.delivery.order_ms.infra.adapters.inbound.web.presenter.dto.ordercontroller.update.OrderUpdateRequestDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderFacade orderFacade;

    public OrderController(OrderFacade orderFacade) {
        this.orderFacade = orderFacade;
    }

    @PostMapping
    public ResponseEntity<OrderResponseDto> create(
            @Valid @RequestBody OrderCreateRequestDto request,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderFacade.create(request, authorization));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderResponseDto> update(
            @PathVariable UUID id,
            @Valid @RequestBody OrderUpdateRequestDto request,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization
    ) {
        return ResponseEntity.ok(orderFacade.update(id, request, authorization));
    }

    @PatchMapping("/{id}/finalize")
    public ResponseEntity<OrderResponseDto> finalizeOrder(@PathVariable UUID id) {
        return ResponseEntity.ok(orderFacade.finalizeOrder(id));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<OrderResponseDto> updateStatus(
            @PathVariable UUID id,
            @Valid @RequestBody OrderStatusUpdateRequestDto request
    ) {
        return ResponseEntity.ok(orderFacade.updateStatus(id, request));
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<OrderResponseDto> cancel(@PathVariable UUID id) {
        return ResponseEntity.ok(orderFacade.cancel(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDto> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(orderFacade.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<OrderResponseDto>> findAll(
            @RequestParam(required = false) UUID restaurantId
    ) {
        return ResponseEntity.ok(orderFacade.findAll(restaurantId));
    }
}
