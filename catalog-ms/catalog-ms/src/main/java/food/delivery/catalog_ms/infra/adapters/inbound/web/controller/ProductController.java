package food.delivery.catalog_ms.infra.adapters.inbound.web.controller;

import food.delivery.catalog_ms.infra.adapters.inbound.web.facade.ProductFacade;
import food.delivery.catalog_ms.infra.adapters.inbound.web.presenter.dto.productcontroller.create.ProductCreateRequestDto;
import food.delivery.catalog_ms.infra.adapters.inbound.web.presenter.dto.productcontroller.get.ProductResponseDto;
import food.delivery.catalog_ms.infra.adapters.inbound.web.presenter.dto.productcontroller.resolve.ProductResolveRequestDto;
import food.delivery.catalog_ms.infra.adapters.inbound.web.presenter.dto.productcontroller.resolve.ProductResolveResponseDto;
import food.delivery.catalog_ms.infra.adapters.inbound.web.presenter.dto.productcontroller.update.ProductUpdateRequestDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductFacade productFacade;

    public ProductController(ProductFacade productFacade) {
        this.productFacade = productFacade;
    }

    @PostMapping
    public ResponseEntity<ProductResponseDto> create(@Valid @RequestBody ProductCreateRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productFacade.create(request));
    }

    @GetMapping
    public ResponseEntity<List<ProductResponseDto>> findByRestaurant(
            @RequestParam UUID restaurantId,
            @RequestParam(required = false) String search
    ) {
        return ResponseEntity.ok(productFacade.findByRestaurant(restaurantId, search));
    }

    @PostMapping("/resolve")
    public ResponseEntity<ProductResolveResponseDto> resolve(@Valid @RequestBody ProductResolveRequestDto request) {
        return ResponseEntity.ok(productFacade.resolve(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(productFacade.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDto> update(
            @PathVariable UUID id,
            @Valid @RequestBody ProductUpdateRequestDto request
    ) {
        return ResponseEntity.ok(productFacade.update(id, request));
    }

    @PutMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductResponseDto> uploadImage(
            @PathVariable UUID id,
            @RequestPart("file") MultipartFile file
    ) {
        return ResponseEntity.ok(productFacade.uploadImage(id, file));
    }

    @DeleteMapping("/{id}/image")
    public ResponseEntity<ProductResponseDto> deleteImage(@PathVariable UUID id) {
        return ResponseEntity.ok(productFacade.deleteImage(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        productFacade.delete(id);
        return ResponseEntity.noContent().build();
    }
}
