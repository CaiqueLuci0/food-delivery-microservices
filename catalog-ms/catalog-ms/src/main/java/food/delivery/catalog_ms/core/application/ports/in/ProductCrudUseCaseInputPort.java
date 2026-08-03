package food.delivery.catalog_ms.core.application.ports.in;

import food.delivery.catalog_ms.core.domain.entities.Product;

import java.io.InputStream;
import java.util.List;
import java.util.UUID;

public interface ProductCrudUseCaseInputPort {

    Product create(UUID authenticatedUserId, Product product);

    Product findById(UUID id);

    List<Product> findByRestaurantId(UUID restaurantId, String search);

    Product update(
            UUID authenticatedUserId,
            UUID productId,
            Product product,
            boolean replaceSpecifications
    );

    Product uploadImage(
            UUID authenticatedUserId,
            UUID productId,
            InputStream body,
            long contentLength,
            String contentType
    );

    Product deleteImage(UUID authenticatedUserId, UUID productId);

    void delete(UUID authenticatedUserId, UUID productId);
}
