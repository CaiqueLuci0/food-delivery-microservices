package food.delivery.order_ms.core.application.ports.out;

import food.delivery.order_ms.core.domain.entities.CatalogResolution;

import java.util.List;
import java.util.UUID;

public interface CatalogResolveOutputPort {

    CatalogResolution resolve(List<ResolveItem> items, String bearerToken);

    record ResolveItem(UUID productId, List<UUID> specOptionIds) {
    }
}
