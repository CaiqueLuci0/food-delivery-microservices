package food.delivery.catalog_ms.infra.adapters.outbound.persistence.mappers;

import food.delivery.catalog_ms.core.domain.entities.Product;
import food.delivery.catalog_ms.core.domain.entities.SpecOption;
import food.delivery.catalog_ms.core.domain.entities.Specification;
import food.delivery.catalog_ms.infra.adapters.outbound.persistence.jpaentities.JpaProduct;
import food.delivery.catalog_ms.infra.adapters.outbound.persistence.jpaentities.JpaSpecOption;
import food.delivery.catalog_ms.infra.adapters.outbound.persistence.jpaentities.JpaSpecification;

import java.util.ArrayList;
import java.util.List;

public final class ProductPersistenceMapper {

    private ProductPersistenceMapper() {
    }

    public static JpaProduct toJpa(Product product) {
        if (product == null) {
            return null;
        }
        JpaProduct jpaProduct = new JpaProduct();
        jpaProduct.setId(product.getId());
        jpaProduct.setName(product.getName());
        jpaProduct.setPrice(product.getPrice());
        jpaProduct.setDescription(product.getDescription());
        jpaProduct.setOwnerId(product.getOwnerId());
        jpaProduct.setRestaurantId(product.getRestaurantId());
        jpaProduct.setImageKey(product.getImageKey());

        List<JpaSpecification> jpaSpecifications = new ArrayList<>();
        if (product.getSpecifications() != null) {
            for (Specification specification : product.getSpecifications()) {
                jpaSpecifications.add(toJpaSpecification(specification, jpaProduct));
            }
        }
        jpaProduct.setSpecifications(jpaSpecifications);
        return jpaProduct;
    }

    public static void mergeScalars(Product product, JpaProduct jpaProduct) {
        jpaProduct.setName(product.getName());
        jpaProduct.setPrice(product.getPrice());
        jpaProduct.setDescription(product.getDescription());
        jpaProduct.setOwnerId(product.getOwnerId());
        jpaProduct.setRestaurantId(product.getRestaurantId());
        jpaProduct.setImageKey(product.getImageKey());
    }

    public static void replaceSpecifications(Product product, JpaProduct jpaProduct) {
        if (product.getSpecifications() == null) {
            return;
        }
        for (Specification specification : product.getSpecifications()) {
            jpaProduct.getSpecifications().add(toNewJpaSpecification(specification, jpaProduct));
        }
    }

    public static void mergeIntoJpa(Product product, JpaProduct jpaProduct) {
        mergeScalars(product, jpaProduct);
        jpaProduct.getSpecifications().clear();
        replaceSpecifications(product, jpaProduct);
    }

    public static Product toDomain(JpaProduct jpaProduct) {
        if (jpaProduct == null) {
            return null;
        }
        Product product = new Product();
        product.setId(jpaProduct.getId());
        product.setName(jpaProduct.getName());
        product.setPrice(jpaProduct.getPrice());
        product.setDescription(jpaProduct.getDescription());
        product.setOwnerId(jpaProduct.getOwnerId());
        product.setRestaurantId(jpaProduct.getRestaurantId());
        product.setImageKey(jpaProduct.getImageKey());

        List<Specification> specifications = new ArrayList<>();
        if (jpaProduct.getSpecifications() != null) {
            for (JpaSpecification jpaSpecification : jpaProduct.getSpecifications()) {
                specifications.add(toDomainSpecification(jpaSpecification, product));
            }
        }
        product.setSpecifications(specifications);
        return product;
    }

    public static SpecOption toDomainOption(JpaSpecOption jpaSpecOption) {
        if (jpaSpecOption == null) {
            return null;
        }
        SpecOption option = new SpecOption();
        option.setId(jpaSpecOption.getId());
        option.setName(jpaSpecOption.getName());
        option.setDescription(jpaSpecOption.getDescription());
        option.setExtraPrice(jpaSpecOption.getExtraPrice());
        return option;
    }

    private static JpaSpecification toJpaSpecification(Specification specification, JpaProduct jpaProduct) {
        JpaSpecification jpaSpecification = new JpaSpecification();
        jpaSpecification.setId(specification.getId());
        jpaSpecification.setName(specification.getName());
        jpaSpecification.setDescription(specification.getDescription());
        jpaSpecification.setProduct(jpaProduct);

        List<JpaSpecOption> jpaOptions = new ArrayList<>();
        if (specification.getSpecOptions() != null) {
            for (SpecOption option : specification.getSpecOptions()) {
                jpaOptions.add(toJpaOption(option, jpaSpecification));
            }
        }
        jpaSpecification.setSpecOptions(jpaOptions);
        return jpaSpecification;
    }

    private static JpaSpecification toNewJpaSpecification(Specification specification, JpaProduct jpaProduct) {
        JpaSpecification jpaSpecification = new JpaSpecification();
        jpaSpecification.setId(null);
        jpaSpecification.setName(specification.getName());
        jpaSpecification.setDescription(specification.getDescription());
        jpaSpecification.setProduct(jpaProduct);

        List<JpaSpecOption> jpaOptions = new ArrayList<>();
        if (specification.getSpecOptions() != null) {
            for (SpecOption option : specification.getSpecOptions()) {
                jpaOptions.add(toNewJpaOption(option, jpaSpecification));
            }
        }
        jpaSpecification.setSpecOptions(jpaOptions);
        return jpaSpecification;
    }

    private static JpaSpecOption toJpaOption(SpecOption option, JpaSpecification jpaSpecification) {
        JpaSpecOption jpaOption = new JpaSpecOption();
        jpaOption.setId(option.getId());
        jpaOption.setName(option.getName());
        jpaOption.setDescription(option.getDescription());
        jpaOption.setExtraPrice(option.getExtraPrice());
        jpaOption.setSpecification(jpaSpecification);
        return jpaOption;
    }

    private static JpaSpecOption toNewJpaOption(SpecOption option, JpaSpecification jpaSpecification) {
        JpaSpecOption jpaOption = new JpaSpecOption();
        jpaOption.setId(null);
        jpaOption.setName(option.getName());
        jpaOption.setDescription(option.getDescription());
        jpaOption.setExtraPrice(option.getExtraPrice());
        jpaOption.setSpecification(jpaSpecification);
        return jpaOption;
    }

    private static Specification toDomainSpecification(JpaSpecification jpaSpecification, Product product) {
        Specification specification = new Specification();
        specification.setId(jpaSpecification.getId());
        specification.setName(jpaSpecification.getName());
        specification.setDescription(jpaSpecification.getDescription());
        specification.setProduct(product);

        List<SpecOption> options = new ArrayList<>();
        if (jpaSpecification.getSpecOptions() != null) {
            for (JpaSpecOption jpaOption : jpaSpecification.getSpecOptions()) {
                SpecOption option = toDomainOption(jpaOption);
                option.setSpecification(specification);
                options.add(option);
            }
        }
        specification.setSpecOptions(options);
        return specification;
    }
}
