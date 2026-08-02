package food.delivery.catalog_ms.core.application.ports.in;

import food.delivery.catalog_ms.core.domain.entities.Product;
import food.delivery.catalog_ms.core.domain.entities.SpecOption;

import java.util.List;
import java.util.UUID;

public interface ProductResolveUseCaseInputPort {

    ResolvedProduct resolve(UUID productId, List<UUID> specOptionIds);

    record ResolvedProduct(Product product, List<SpecOption> specOptions) {
    }
}
