package food.delivery.catalog_ms.infra.adapters.inbound.web.facade;

import food.delivery.catalog_ms.core.application.ports.in.ProductCrudUseCaseInputPort;
import food.delivery.catalog_ms.core.application.ports.in.ProductResolveUseCaseInputPort;
import food.delivery.catalog_ms.core.domain.entities.Product;
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

import java.util.List;
import java.util.UUID;

@Component
public class ProductFacade {

    private final ProductCrudUseCaseInputPort productCrudUseCase;
    private final ProductResolveUseCaseInputPort productResolveUseCase;

    public ProductFacade(
            ProductCrudUseCaseInputPort productCrudUseCase,
            ProductResolveUseCaseInputPort productResolveUseCase
    ) {
        this.productCrudUseCase = productCrudUseCase;
        this.productResolveUseCase = productResolveUseCase;
    }

    @Transactional
    public ProductResponseDto create(ProductCreateRequestDto request) {
        Product created = productCrudUseCase.create(
                AuthenticatedUser.requireId(),
                ProductCreateMapper.toProduct(request)
        );
        return ProductResponseMapper.toResponse(created);
    }

    public List<ProductResponseDto> findByRestaurant(UUID restaurantId, String search) {
        return ProductResponseMapper.toResponseList(
                productCrudUseCase.findByRestaurantId(restaurantId, search)
        );
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
            dto.setProduct(ProductResponseMapper.toResponse(item.product()));
            dto.setSpecOptions(
                    item.specOptions().stream().map(ProductResponseMapper::toOptionResponse).toList()
            );
            return dto;
        }).toList());
        return response;
    }

    public ProductResponseDto findById(UUID id) {
        return ProductResponseMapper.toResponse(productCrudUseCase.findById(id));
    }

    @Transactional
    public ProductResponseDto update(UUID id, ProductUpdateRequestDto request) {
        Product updated = productCrudUseCase.update(
                AuthenticatedUser.requireId(),
                id,
                ProductUpdateMapper.toProduct(request),
                request.isSpecificationsPresent()
        );
        return ProductResponseMapper.toResponse(updated);
    }

    @Transactional
    public void delete(UUID id) {
        productCrudUseCase.delete(AuthenticatedUser.requireId(), id);
    }
}
