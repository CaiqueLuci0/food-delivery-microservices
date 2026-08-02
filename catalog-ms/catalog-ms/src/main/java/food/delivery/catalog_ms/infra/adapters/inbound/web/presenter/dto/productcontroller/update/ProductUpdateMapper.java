package food.delivery.catalog_ms.infra.adapters.inbound.web.presenter.dto.productcontroller.update;

import food.delivery.catalog_ms.core.domain.entities.Product;
import food.delivery.catalog_ms.infra.adapters.inbound.web.presenter.dto.productcontroller.create.ProductCreateMapper;

public final class ProductUpdateMapper {

    private ProductUpdateMapper() {
    }

    public static Product toProduct(ProductUpdateRequestDto request) {
        Product product = new Product();
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setDescription(request.getDescription());
        if (request.isSpecificationsPresent()) {
            product.setSpecifications(ProductCreateMapper.toSpecifications(request.getSpecifications()));
        }
        return product;
    }
}
