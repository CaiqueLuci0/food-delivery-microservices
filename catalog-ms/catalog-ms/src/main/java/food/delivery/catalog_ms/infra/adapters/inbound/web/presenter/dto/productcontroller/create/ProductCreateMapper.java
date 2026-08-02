package food.delivery.catalog_ms.infra.adapters.inbound.web.presenter.dto.productcontroller.create;

import food.delivery.catalog_ms.core.domain.entities.Product;
import food.delivery.catalog_ms.core.domain.entities.SpecOption;
import food.delivery.catalog_ms.core.domain.entities.Specification;

import java.util.ArrayList;
import java.util.List;

public final class ProductCreateMapper {

    private ProductCreateMapper() {
    }

    public static Product toProduct(ProductCreateRequestDto request) {
        Product product = new Product();
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setDescription(request.getDescription());
        product.setSpecifications(toSpecifications(request.getSpecifications()));
        return product;
    }

    public static List<Specification> toSpecifications(List<SpecificationRequestDto> requests) {
        List<Specification> specifications = new ArrayList<>();
        if (requests == null) {
            return specifications;
        }
        for (SpecificationRequestDto request : requests) {
            Specification specification = new Specification();
            specification.setName(request.getName());
            specification.setDescription(request.getDescription());
            specification.setSpecOptions(toOptions(request.getSpecOptions()));
            specifications.add(specification);
        }
        return specifications;
    }

    private static List<SpecOption> toOptions(List<SpecOptionRequestDto> requests) {
        List<SpecOption> options = new ArrayList<>();
        if (requests == null) {
            return options;
        }
        for (SpecOptionRequestDto request : requests) {
            SpecOption option = new SpecOption();
            option.setName(request.getName());
            option.setDescription(request.getDescription());
            option.setExtraPrice(request.getExtraPrice());
            options.add(option);
        }
        return options;
    }
}
