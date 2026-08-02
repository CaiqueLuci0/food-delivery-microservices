package food.delivery.order_ms.infra.adapters.outbound.http.catalog;

import food.delivery.order_ms.core.application.ports.out.CatalogResolveOutputPort;
import food.delivery.order_ms.core.domain.entities.CatalogResolution;
import food.delivery.order_ms.core.domain.entities.ProductSnapshot;
import food.delivery.order_ms.core.domain.entities.SpecOptionSnapshot;
import food.delivery.order_ms.core.domain.enums.ConstMessagesEnum;
import food.delivery.order_ms.core.domain.exceptions.ConflictException;
import food.delivery.order_ms.core.domain.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.util.ArrayList;
import java.util.List;

@Component
public class CatalogResolveAdapter implements CatalogResolveOutputPort {

    private final RestClient catalogRestClient;

    public CatalogResolveAdapter(@Qualifier("catalogRestClient") RestClient catalogRestClient) {
        this.catalogRestClient = catalogRestClient;
    }

    @Override
    public CatalogResolution resolve(List<ResolveItem> items, String bearerToken) {
        CatalogResolveRequestDto request = new CatalogResolveRequestDto();
        List<CatalogResolveRequestDto.ResolveItemDto> requestItems = new ArrayList<>();
        for (ResolveItem item : items) {
            CatalogResolveRequestDto.ResolveItemDto dto = new CatalogResolveRequestDto.ResolveItemDto();
            dto.setProductId(item.productId());
            dto.setSpecOptionIds(item.specOptionIds());
            requestItems.add(dto);
        }
        request.setItems(requestItems);

        try {
            CatalogResolveResponseDto response = catalogRestClient.post()
                    .uri("/products/resolve")
                    .header(HttpHeaders.AUTHORIZATION, bearerAuthorization(bearerToken))
                    .body(request)
                    .retrieve()
                    .body(CatalogResolveResponseDto.class);

            if (response == null || response.getItems() == null) {
                throw new ConflictException(ConstMessagesEnum.CATALOG_RESOLVE_FAILED.getMessage());
            }

            return toDomain(response);
        } catch (RestClientResponseException e) {
            HttpStatusCode status = e.getStatusCode();
            if (status.value() == 404) {
                throw new NotFoundException(ConstMessagesEnum.NOT_FOUND.getMessage());
            }
            if (status.value() == 409) {
                throw new ConflictException(ConstMessagesEnum.INVALID_REQUEST.getMessage());
            }
            throw new ConflictException(ConstMessagesEnum.CATALOG_RESOLVE_FAILED.getMessage());
        }
    }

    private CatalogResolution toDomain(CatalogResolveResponseDto response) {
        List<ProductSnapshot> productSnapshots = new ArrayList<>();
        for (CatalogResolveResponseDto.ResolvedItemDto item : response.getItems()) {
            if (item.getProduct() == null) {
                throw new NotFoundException(ConstMessagesEnum.NOT_FOUND.getMessage());
            }
            CatalogResolveResponseDto.ProductDto product = item.getProduct();
            ProductSnapshot snapshot = new ProductSnapshot();
            snapshot.setName(product.getName());
            snapshot.setPrice(product.getPrice());
            snapshot.setDescription(product.getDescription());

            List<SpecOptionSnapshot> optionSnapshots = new ArrayList<>();
            if (item.getSpecOptions() != null) {
                for (CatalogResolveResponseDto.SpecOptionDto option : item.getSpecOptions()) {
                    SpecOptionSnapshot optionSnapshot = new SpecOptionSnapshot();
                    optionSnapshot.setName(option.getName());
                    optionSnapshot.setDescription(option.getDescription());
                    optionSnapshot.setExtraPrice(option.getExtraPrice());
                    optionSnapshot.setProductSnapshot(snapshot);
                    optionSnapshots.add(optionSnapshot);
                }
            }
            snapshot.setSpecOptionSnapshots(optionSnapshots);
            productSnapshots.add(snapshot);
        }

        return new CatalogResolution(
                response.getRestaurantId(),
                response.getOwnerId(),
                productSnapshots
        );
    }

    private String bearerAuthorization(String bearerToken) {
        if (bearerToken == null || bearerToken.isBlank()) {
            return null;
        }
        if (bearerToken.startsWith("Bearer ")) {
            return bearerToken;
        }
        return "Bearer " + bearerToken;
    }
}
