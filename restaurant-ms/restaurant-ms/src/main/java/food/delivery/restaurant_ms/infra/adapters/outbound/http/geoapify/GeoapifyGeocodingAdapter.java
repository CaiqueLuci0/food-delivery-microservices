package food.delivery.restaurant_ms.infra.adapters.outbound.http.geoapify;

import food.delivery.restaurant_ms.core.application.ports.out.GeocodingOutputPort;
import food.delivery.restaurant_ms.core.domain.entities.Address;
import food.delivery.restaurant_ms.core.domain.entities.GeoCoordinates;
import food.delivery.restaurant_ms.infra.config.GeoapifyProperties;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class GeoapifyGeocodingAdapter implements GeocodingOutputPort {

    private final RestClient geoapifyRestClient;
    private final GeoapifyProperties geoapifyProperties;

    public GeoapifyGeocodingAdapter(
            @Qualifier("geoapifyRestClient") RestClient geoapifyRestClient,
            GeoapifyProperties geoapifyProperties
    ) {
        this.geoapifyRestClient = geoapifyRestClient;
        this.geoapifyProperties = geoapifyProperties;
    }

    @Override
    public Optional<GeoCoordinates> geocode(Address address) {
        if (address == null) {
            return Optional.empty();
        }

        String text = buildSearchText(address);
        if (text.isBlank()) {
            return Optional.empty();
        }

        try {
            URI uri = UriComponentsBuilder
                    .fromPath("/v1/geocode/search")
                    .queryParam("text", text)
                    .queryParam("apiKey", geoapifyProperties.getApiKey())
                    .build()
                    .encode()
                    .toUri();

            GeoapifyGeocodeResponse response = geoapifyRestClient.get()
                    .uri(uri)
                    .retrieve()
                    .body(GeoapifyGeocodeResponse.class);

            if (response == null || response.getFeatures() == null || response.getFeatures().isEmpty()) {
                return Optional.empty();
            }

            GeoapifyGeocodeResponse.Properties properties = response.getFeatures().get(0).getProperties();
            if (properties == null || properties.getLat() == null || properties.getLon() == null) {
                return Optional.empty();
            }

            return Optional.of(new GeoCoordinates(properties.getLat(), properties.getLon()));
        } catch (RestClientException e) {
            return Optional.empty();
        }
    }

    private String buildSearchText(Address address) {
        return Stream.of(
                        address.getLogradouro(),
                        address.getNumero(),
                        address.getBairro(),
                        address.getCidade(),
                        address.getUf(),
                        address.getCep(),
                        "Brazil"
                )
                .filter(part -> part != null && !part.isBlank())
                .collect(Collectors.joining(", "));
    }
}
