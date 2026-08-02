package food.delivery.catalog_ms.core.application.usecases;

import food.delivery.catalog_ms.core.application.ports.in.ProductCrudUseCaseInputPort;
import food.delivery.catalog_ms.core.application.ports.out.ProductRepositoryOutputPort;
import food.delivery.catalog_ms.core.application.ports.out.RestaurantReferenceRepositoryOutputPort;
import food.delivery.catalog_ms.core.domain.entities.Product;
import food.delivery.catalog_ms.core.domain.entities.RestaurantReference;
import food.delivery.catalog_ms.core.domain.entities.SpecOption;
import food.delivery.catalog_ms.core.domain.entities.Specification;
import food.delivery.catalog_ms.core.domain.enums.ConstMessagesEnum;
import food.delivery.catalog_ms.core.domain.exceptions.ConflictException;
import food.delivery.catalog_ms.core.domain.exceptions.ForbiddenException;
import food.delivery.catalog_ms.core.domain.exceptions.NotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ProductCrudUseCase implements ProductCrudUseCaseInputPort {

    private final ProductRepositoryOutputPort productRepositoryOutputPort;
    private final RestaurantReferenceRepositoryOutputPort restaurantReferenceRepositoryOutputPort;

    public ProductCrudUseCase(
            ProductRepositoryOutputPort productRepositoryOutputPort,
            RestaurantReferenceRepositoryOutputPort restaurantReferenceRepositoryOutputPort
    ) {
        this.productRepositoryOutputPort = productRepositoryOutputPort;
        this.restaurantReferenceRepositoryOutputPort = restaurantReferenceRepositoryOutputPort;
    }

    @Override
    public Product create(UUID authenticatedUserId, Product product) {
        RestaurantReference reference = restaurantReferenceRepositoryOutputPort.findByOwnerId(authenticatedUserId)
                .orElseThrow(() -> new ConflictException(
                        ConstMessagesEnum.RESTAURANT_REFERENCE_NOT_FOUND.getMessage()
                ));

        product.setOwnerId(reference.getOwnerId());
        product.setRestaurantId(reference.getRestaurantId());
        wireSpecifications(product, product.getSpecifications());
        return productRepositoryOutputPort.save(product);
    }

    @Override
    public Product findById(UUID id) {
        return productRepositoryOutputPort.findById(id)
                .orElseThrow(() -> new NotFoundException(ConstMessagesEnum.NOT_FOUND.getMessage()));
    }

    @Override
    public List<Product> findByRestaurantId(UUID restaurantId, String search) {
        if (restaurantId == null) {
            throw new ConflictException(ConstMessagesEnum.INVALID_REQUEST.getMessage());
        }
        String normalizedSearch = (search == null || search.isBlank()) ? null : search.trim();
        return productRepositoryOutputPort.findByRestaurantId(restaurantId, normalizedSearch);
    }

    @Override
    public Product update(
            UUID authenticatedUserId,
            UUID productId,
            Product product,
            boolean replaceSpecifications
    ) {
        Product existing = findById(productId);
        assertOwner(authenticatedUserId, existing);

        existing.setName(product.getName());
        existing.setPrice(product.getPrice());
        existing.setDescription(product.getDescription());

        if (replaceSpecifications) {
            List<Specification> replacements = product.getSpecifications() != null
                    ? product.getSpecifications()
                    : new ArrayList<>();
            for (Specification specification : replacements) {
                specification.setId(null);
                if (specification.getSpecOptions() != null) {
                    for (SpecOption option : specification.getSpecOptions()) {
                        option.setId(null);
                    }
                }
            }
            wireSpecifications(existing, replacements);
        }

        return productRepositoryOutputPort.save(existing, replaceSpecifications);
    }

    @Override
    public void delete(UUID authenticatedUserId, UUID productId) {
        Product existing = findById(productId);
        assertOwner(authenticatedUserId, existing);
        productRepositoryOutputPort.delete(existing);
    }

    private void wireSpecifications(Product product, List<Specification> specifications) {
        if (specifications == null) {
            product.setSpecifications(new ArrayList<>());
            return;
        }
        for (Specification specification : specifications) {
            specification.setProduct(product);
            List<SpecOption> options = specification.getSpecOptions();
            if (options == null) {
                specification.setSpecOptions(new ArrayList<>());
                continue;
            }
            for (SpecOption option : options) {
                option.setSpecification(specification);
            }
        }
        product.setSpecifications(specifications);
    }

    private void assertOwner(UUID authenticatedUserId, Product product) {
        if (authenticatedUserId == null || !authenticatedUserId.equals(product.getOwnerId())) {
            throw new ForbiddenException(ConstMessagesEnum.ACCESS_DENIED.getMessage());
        }
    }
}
