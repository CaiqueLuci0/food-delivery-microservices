package food.delivery.catalog_ms.infra.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.messaging")
public class MessagingProperties {

    private String restaurantCreatedQueue;
    private String restaurantDeletedQueue;

    public String getRestaurantCreatedQueue() {
        return restaurantCreatedQueue;
    }

    public void setRestaurantCreatedQueue(String restaurantCreatedQueue) {
        this.restaurantCreatedQueue = restaurantCreatedQueue;
    }

    public String getRestaurantDeletedQueue() {
        return restaurantDeletedQueue;
    }

    public void setRestaurantDeletedQueue(String restaurantDeletedQueue) {
        this.restaurantDeletedQueue = restaurantDeletedQueue;
    }
}
