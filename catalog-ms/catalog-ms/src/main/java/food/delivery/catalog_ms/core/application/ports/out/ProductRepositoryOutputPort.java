package food.delivery.catalog_ms.core.application.ports.out;

import food.delivery.catalog_ms.core.domain.entities.Product;
import food.delivery.catalog_ms.core.domain.entities.SpecOption;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepositoryOutputPort {

    Product save(Product product);

    Product save(Product product, boolean replaceSpecifications);

    Optional<Product> findById(UUID id);

    List<Product> findByRestaurantId(UUID restaurantId, String search);

    void delete(Product product);

    Optional<SpecOption> findSpecOptionById(UUID specOptionId);
}
