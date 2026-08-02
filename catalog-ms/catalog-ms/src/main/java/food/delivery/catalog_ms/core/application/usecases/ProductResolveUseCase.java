package food.delivery.catalog_ms.core.application.usecases;

import food.delivery.catalog_ms.core.application.ports.in.ProductResolveUseCaseInputPort;
import food.delivery.catalog_ms.core.application.ports.out.ProductRepositoryOutputPort;
import food.delivery.catalog_ms.core.domain.entities.Product;
import food.delivery.catalog_ms.core.domain.entities.SpecOption;
import food.delivery.catalog_ms.core.domain.enums.ConstMessagesEnum;
import food.delivery.catalog_ms.core.domain.exceptions.NotFoundException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class ProductResolveUseCase implements ProductResolveUseCaseInputPort {

    private final ProductRepositoryOutputPort productRepositoryOutputPort;

    public ProductResolveUseCase(ProductRepositoryOutputPort productRepositoryOutputPort) {
        this.productRepositoryOutputPort = productRepositoryOutputPort;
    }

    @Override
    public ResolvedProduct resolve(UUID productId, List<UUID> specOptionIds) {
        Product product = productRepositoryOutputPort.findById(productId)
                .orElseThrow(() -> new NotFoundException(ConstMessagesEnum.NOT_FOUND.getMessage()));

        List<UUID> requestedIds = specOptionIds != null ? specOptionIds : List.of();
        Set<UUID> productOptionIds = collectOptionIds(product);
        List<SpecOption> resolvedOptions = new ArrayList<>();

        for (UUID optionId : requestedIds) {
            if (optionId == null || !productOptionIds.contains(optionId)) {
                throw new NotFoundException(ConstMessagesEnum.NOT_FOUND.getMessage());
            }
            SpecOption option = productRepositoryOutputPort.findSpecOptionById(optionId)
                    .orElseThrow(() -> new NotFoundException(ConstMessagesEnum.NOT_FOUND.getMessage()));
            resolvedOptions.add(option);
        }

        return new ResolvedProduct(product, resolvedOptions);
    }

    private Set<UUID> collectOptionIds(Product product) {
        Set<UUID> ids = new HashSet<>();
        if (product.getSpecifications() == null) {
            return ids;
        }
        for (var specification : product.getSpecifications()) {
            if (specification.getSpecOptions() == null) {
                continue;
            }
            for (SpecOption option : specification.getSpecOptions()) {
                if (option.getId() != null) {
                    ids.add(option.getId());
                }
            }
        }
        return ids;
    }
}
