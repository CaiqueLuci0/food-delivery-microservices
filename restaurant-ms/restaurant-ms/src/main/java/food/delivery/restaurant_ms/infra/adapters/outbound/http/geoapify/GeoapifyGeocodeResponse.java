package food.delivery.restaurant_ms.infra.adapters.outbound.http.geoapify;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GeoapifyGeocodeResponse {

    private List<Feature> features;

    public List<Feature> getFeatures() {
        return features;
    }

    public void setFeatures(List<Feature> features) {
        this.features = features;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Feature {
        private Properties properties;

        public Properties getProperties() {
            return properties;
        }

        public void setProperties(Properties properties) {
            this.properties = properties;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Properties {
        private BigDecimal lat;
        private BigDecimal lon;

        public BigDecimal getLat() {
            return lat;
        }

        public void setLat(BigDecimal lat) {
            this.lat = lat;
        }

        public BigDecimal getLon() {
            return lon;
        }

        public void setLon(BigDecimal lon) {
            this.lon = lon;
        }
    }
}
