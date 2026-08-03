package food.delivery.catalog_ms.infra.adapters.inbound.web.presenter.dto.productcontroller.get;

import food.delivery.catalog_ms.core.domain.entities.Product;
import food.delivery.catalog_ms.core.domain.entities.SpecOption;
import food.delivery.catalog_ms.core.domain.entities.Specification;

import java.util.ArrayList;
import java.util.List;

public final class ProductResponseMapper {

    private ProductResponseMapper() {
    }

    public static ProductResponseDto toResponse(Product product) {
        ProductResponseDto response = new ProductResponseDto();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setPrice(product.getPrice());
        response.setDescription(product.getDescription());
        response.setOwnerId(product.getOwnerId());
        response.setRestaurantId(product.getRestaurantId());
        response.setImageKey(product.getImageKey());
        response.setSpecifications(toSpecifications(product.getSpecifications()));
        return response;
    }

    public static List<ProductResponseDto> toResponseList(List<Product> products) {
        return products.stream().map(ProductResponseMapper::toResponse).toList();
    }

    public static SpecOptionResponseDto toOptionResponse(SpecOption option) {
        SpecOptionResponseDto response = new SpecOptionResponseDto();
        response.setId(option.getId());
        response.setName(option.getName());
        response.setDescription(option.getDescription());
        response.setExtraPrice(option.getExtraPrice());
        return response;
    }

    private static List<SpecificationResponseDto> toSpecifications(List<Specification> specifications) {
        List<SpecificationResponseDto> responses = new ArrayList<>();
        if (specifications == null) {
            return responses;
        }
        for (Specification specification : specifications) {
            SpecificationResponseDto response = new SpecificationResponseDto();
            response.setId(specification.getId());
            response.setName(specification.getName());
            response.setDescription(specification.getDescription());
            List<SpecOptionResponseDto> options = new ArrayList<>();
            if (specification.getSpecOptions() != null) {
                for (SpecOption option : specification.getSpecOptions()) {
                    options.add(toOptionResponse(option));
                }
            }
            response.setSpecOptions(options);
            responses.add(response);
        }
        return responses;
    }
}
