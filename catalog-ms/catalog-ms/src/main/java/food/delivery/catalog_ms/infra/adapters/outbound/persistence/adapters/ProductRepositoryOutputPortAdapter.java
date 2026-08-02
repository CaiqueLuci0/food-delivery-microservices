package food.delivery.catalog_ms.infra.adapters.outbound.persistence.adapters;

import food.delivery.catalog_ms.core.application.ports.out.ProductRepositoryOutputPort;
import food.delivery.catalog_ms.core.domain.entities.Product;
import food.delivery.catalog_ms.core.domain.entities.SpecOption;
import food.delivery.catalog_ms.infra.adapters.outbound.persistence.jpaentities.JpaProduct;
import food.delivery.catalog_ms.infra.adapters.outbound.persistence.jparepositories.JpaProductRepository;
import food.delivery.catalog_ms.infra.adapters.outbound.persistence.jparepositories.JpaSpecOptionRepository;
import food.delivery.catalog_ms.infra.adapters.outbound.persistence.mappers.ProductPersistenceMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class ProductRepositoryOutputPortAdapter implements ProductRepositoryOutputPort {

    private final JpaProductRepository jpaProductRepository;
    private final JpaSpecOptionRepository jpaSpecOptionRepository;

    public ProductRepositoryOutputPortAdapter(
            JpaProductRepository jpaProductRepository,
            JpaSpecOptionRepository jpaSpecOptionRepository
    ) {
        this.jpaProductRepository = jpaProductRepository;
        this.jpaSpecOptionRepository = jpaSpecOptionRepository;
    }

    @Override
    public Product save(Product product) {
        return save(product, false);
    }

    @Override
    public Product save(Product product, boolean replaceSpecifications) {
        JpaProduct jpaProduct;
        if (product.getId() != null) {
            jpaProduct = jpaProductRepository.findByIdWithDetails(product.getId())
                    .orElseGet(JpaProduct::new);
            ProductPersistenceMapper.mergeScalars(product, jpaProduct);
            if (replaceSpecifications) {
                jpaProduct.getSpecifications().clear();
                jpaProductRepository.flush();
                ProductPersistenceMapper.replaceSpecifications(product, jpaProduct);
            }
        } else {
            jpaProduct = ProductPersistenceMapper.toJpa(product);
        }
        return ProductPersistenceMapper.toDomain(jpaProductRepository.save(jpaProduct));
    }

    @Override
    public Optional<Product> findById(UUID id) {
        return jpaProductRepository.findByIdWithDetails(id).map(ProductPersistenceMapper::toDomain);
    }

    @Override
    public List<Product> findByRestaurantId(UUID restaurantId, String search) {
        List<JpaProduct> products = search == null
                ? jpaProductRepository.findByRestaurantIdWithDetails(restaurantId)
                : jpaProductRepository.findByRestaurantIdAndSearch(restaurantId, search);
        return products.stream()
                .map(ProductPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public void delete(Product product) {
        jpaProductRepository.deleteById(product.getId());
    }

    @Override
    public Optional<SpecOption> findSpecOptionById(UUID specOptionId) {
        return jpaSpecOptionRepository.findById(specOptionId).map(ProductPersistenceMapper::toDomainOption);
    }
}
