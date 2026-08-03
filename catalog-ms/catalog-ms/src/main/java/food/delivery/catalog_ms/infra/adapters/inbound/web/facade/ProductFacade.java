package food.delivery.catalog_ms.infra.adapters.inbound.web.facade;

import food.delivery.catalog_ms.core.application.ports.in.ProductCrudUseCaseInputPort;
import food.delivery.catalog_ms.core.application.ports.in.ProductResolveUseCaseInputPort;
import food.delivery.catalog_ms.core.application.ports.out.ObjectStorageOutputPort;
import food.delivery.catalog_ms.core.domain.entities.Product;
import food.delivery.catalog_ms.core.domain.enums.ConstMessagesEnum;
import food.delivery.catalog_ms.core.domain.exceptions.ConflictException;
import food.delivery.catalog_ms.infra.adapters.inbound.web.presenter.dto.productcontroller.create.ProductCreateMapper;
import food.delivery.catalog_ms.infra.adapters.inbound.web.presenter.dto.productcontroller.create.ProductCreateRequestDto;
import food.delivery.catalog_ms.infra.adapters.inbound.web.presenter.dto.productcontroller.get.ProductResponseDto;
import food.delivery.catalog_ms.infra.adapters.inbound.web.presenter.dto.productcontroller.get.ProductResponseMapper;
import food.delivery.catalog_ms.infra.adapters.inbound.web.presenter.dto.productcontroller.resolve.ProductResolveRequestDto;
import food.delivery.catalog_ms.infra.adapters.inbound.web.presenter.dto.productcontroller.resolve.ProductResolveResponseDto;
import food.delivery.catalog_ms.infra.adapters.inbound.web.presenter.dto.productcontroller.update.ProductUpdateMapper;
import food.delivery.catalog_ms.infra.adapters.inbound.web.presenter.dto.productcontroller.update.ProductUpdateRequestDto;
import food.delivery.catalog_ms.infra.adapters.inbound.web.security.AuthenticatedUser;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Component
public class ProductFacade {

    private final ProductCrudUseCaseInputPort productCrudUseCase;
    private final ProductResolveUseCaseInputPort productResolveUseCase;
    private final ObjectStorageOutputPort objectStorageOutputPort;

    public ProductFacade(
            ProductCrudUseCaseInputPort productCrudUseCase,
            ProductResolveUseCaseInputPort productResolveUseCase,
            ObjectStorageOutputPort objectStorageOutputPort
    ) {
        this.productCrudUseCase = productCrudUseCase;
        this.productResolveUseCase = productResolveUseCase;
        this.objectStorageOutputPort = objectStorageOutputPort;
    }

    @Transactional
    public ProductResponseDto create(ProductCreateRequestDto request) {
        Product created = productCrudUseCase.create(
                AuthenticatedUser.requireId(),
                ProductCreateMapper.toProduct(request)
        );
        return toResponse(created);
    }

    public List<ProductResponseDto> findByRestaurant(UUID restaurantId, String search) {
        return productCrudUseCase.findByRestaurantId(restaurantId, search).stream()
                .map(this::toResponse)
                .toList();
    }

    public ProductResolveResponseDto resolve(ProductResolveRequestDto request) {
        List<ProductResolveUseCaseInputPort.ResolveItem> items = request.getItems().stream()
                .map(item -> new ProductResolveUseCaseInputPort.ResolveItem(
                        item.getProductId(),
                        item.getSpecOptionIds()
                ))
                .toList();
        ProductResolveUseCaseInputPort.ResolvedBatch resolved = productResolveUseCase.resolve(items);
        ProductResolveResponseDto response = new ProductResolveResponseDto();
        response.setRestaurantId(resolved.restaurantId());
        response.setOwnerId(resolved.ownerId());
        response.setItems(resolved.items().stream().map(item -> {
            ProductResolveResponseDto.ResolvedItemResponseDto dto =
                    new ProductResolveResponseDto.ResolvedItemResponseDto();
            dto.setProduct(toResponse(item.product()));
            dto.setSpecOptions(
                    item.specOptions().stream().map(ProductResponseMapper::toOptionResponse).toList()
            );
            return dto;
        }).toList());
        return response;
    }

    public ProductResponseDto findById(UUID id) {
        return toResponse(productCrudUseCase.findById(id));
    }

    @Transactional
    public ProductResponseDto update(UUID id, ProductUpdateRequestDto request) {
        Product updated = productCrudUseCase.update(
                AuthenticatedUser.requireId(),
                id,
                ProductUpdateMapper.toProduct(request),
                request.isSpecificationsPresent()
        );
        return toResponse(updated);
    }

    @Transactional
    public ProductResponseDto uploadImage(UUID id, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new ConflictException(ConstMessagesEnum.INVALID_REQUEST.getMessage());
        }
        try {
            Product updated = productCrudUseCase.uploadImage(
                    AuthenticatedUser.requireId(),
                    id,
                    file.getInputStream(),
                    file.getSize(),
                    file.getContentType()
            );
            return toResponse(updated);
        } catch (IOException e) {
            throw new ConflictException(ConstMessagesEnum.INVALID_REQUEST.getMessage());
        }
    }

    @Transactional
    public ProductResponseDto deleteImage(UUID id) {
        return toResponse(productCrudUseCase.deleteImage(AuthenticatedUser.requireId(), id));
    }

    @Transactional
    public void delete(UUID id) {
        productCrudUseCase.delete(AuthenticatedUser.requireId(), id);
    }

    private ProductResponseDto toResponse(Product product) {
        ProductResponseDto dto = ProductResponseMapper.toResponse(product);
        if (dto != null) {
            dto.setImageUrl(objectStorageOutputPort.publicUrl(product.getImageKey()));
        }
        return dto;
    }
}
