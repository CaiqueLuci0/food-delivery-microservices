package food.delivery.restaurant_ms.core.domain.entities;

import java.math.BigDecimal;

public class GeoCoordinates {

    private final BigDecimal latitude;
    private final BigDecimal longitude;

    public GeoCoordinates(BigDecimal latitude, BigDecimal longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }
}
