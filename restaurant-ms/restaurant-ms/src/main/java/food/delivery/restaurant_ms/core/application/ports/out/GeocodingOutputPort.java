package food.delivery.restaurant_ms.core.application.ports.out;

import food.delivery.restaurant_ms.core.domain.entities.Address;
import food.delivery.restaurant_ms.core.domain.entities.GeoCoordinates;

import java.util.Optional;

public interface GeocodingOutputPort {

    Optional<GeoCoordinates> geocode(Address address);
}
