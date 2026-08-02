package food.delivery.payment_ms.infra.adapters.inbound.web.controller;

import food.delivery.payment_ms.infra.adapters.inbound.web.facade.PaymentFacade;
import food.delivery.payment_ms.infra.adapters.inbound.web.presenter.dto.paymentcontroller.PaymentResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentFacade paymentFacade;

    public PaymentController(PaymentFacade paymentFacade) {
        this.paymentFacade = paymentFacade;
    }

    @PatchMapping("/{orderId}/pay")
    public ResponseEntity<PaymentResponseDto> markAsPaid(@PathVariable UUID orderId) {
        return ResponseEntity.ok(paymentFacade.markAsPaid(orderId));
    }
}
