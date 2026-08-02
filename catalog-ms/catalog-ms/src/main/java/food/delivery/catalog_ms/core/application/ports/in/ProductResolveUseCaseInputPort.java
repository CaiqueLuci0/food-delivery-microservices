package food.delivery.catalog_ms.core.application.ports.in;

import food.delivery.catalog_ms.core.domain.entities.Product;
import food.delivery.catalog_ms.core.domain.entities.SpecOption;

import java.util.List;
import java.util.UUID;

public interface ProductResolveUseCaseInputPort {

    ResolvedBatch resolve(List<ResolveItem> items);

    record ResolveItem(UUID productId, List<UUID> specOptionIds) {
    }

    record ResolvedProduct(Product product, List<SpecOption> specOptions) {
    }

    record ResolvedBatch(UUID restaurantId, UUID ownerId, List<ResolvedProduct> items) {
    }
}
