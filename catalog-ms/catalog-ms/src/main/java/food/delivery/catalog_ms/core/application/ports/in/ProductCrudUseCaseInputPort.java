package food.delivery.catalog_ms.core.application.ports.in;

import food.delivery.catalog_ms.core.domain.entities.Product;

import java.util.List;
import java.util.UUID;

public interface ProductCrudUseCaseInputPort {

    Product create(UUID authenticatedUserId, Product product);

    Product findById(UUID id);

    List<Product> findByRestaurantId(UUID restaurantId, String search);

    Product update(UUID authenticatedUserId, UUID productId, Product product, boolean replaceSpecifications);

    void delete(UUID authenticatedUserId, UUID productId);
}
