package food.delivery.catalog_ms.infra.adapters.inbound.web.presenter.dto.productcontroller.resolve;

import food.delivery.catalog_ms.infra.adapters.inbound.web.presenter.dto.productcontroller.get.ProductResponseDto;
import food.delivery.catalog_ms.infra.adapters.inbound.web.presenter.dto.productcontroller.get.SpecOptionResponseDto;

import java.util.ArrayList;
import java.util.List;

public class ProductResolveResponseDto {

    private ProductResponseDto product;
    private List<SpecOptionResponseDto> specOptions = new ArrayList<>();

    public ProductResponseDto getProduct() {
        return product;
    }

    public void setProduct(ProductResponseDto product) {
        this.product = product;
    }

    public List<SpecOptionResponseDto> getSpecOptions() {
        return specOptions;
    }

    public void setSpecOptions(List<SpecOptionResponseDto> specOptions) {
        this.specOptions = specOptions != null ? specOptions : new ArrayList<>();
    }
}
